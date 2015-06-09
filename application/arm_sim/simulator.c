#include <stdarg.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <math.h>
#if !defined(__WIN32) && !defined(__WIN64)
#	include <sys/resource.h>
#endif
/* New versions of MINGW does not seem to require this.
#ifdef __MINGW32__
void *alloca(size_t);
#endif*/
#include <unistd.h>
#include <arm/api.h>
#include <arm/macros.h>
#include <arm/loader.h>
#include <arm/id.h>

/* Sockets Support */
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h> /* close */
#include <netdb.h> /* gethostbyname */
#define INVALID_SOCKET -1
#define SOCKET_ERROR -1
#define closesocket(s) close(s)
typedef int SOCKET;
typedef struct sockaddr_in SOCKADDR_IN;
typedef struct sockaddr SOCKADDR;
typedef struct in_addr IN_ADDR;

/**
 * Display usage of the command.
 * @param prog_name	Program name.
 */
void usage(const char *prog_name) {
	fprintf(stderr, "SYNTAX: %s OPTIONS <exec_name> <exec arguments>\n\n"
			"OPTIONS may be a combination of \n"
			"  -exit=<hexa_address>] : simulation exit address (default symbol _exit)\n"
			"  -h, -help             : display usage message\n"
			"  -start=<hexa_address> : simulation start address (default symbol _start)\n"
			"  -port=<port_number>   : communication port"
			"\n"
			"if args or env strings must be passed to the simulated program,\n"
			"put them in <exec_name>.argv or <exec_name>.envp,\n"
			"one arg or env string on each line, whitespaces will not be ignored,\n"
			"a single '\\n' must be added on the last line of these files\n\n", prog_name);
}


/**
 * Display error.
 * @param fmt		Format string.
 * @param args		Format arguments.
 */
void error_args(const char *fmt, va_list args) {
	fprintf(stderr, "ERROR: ");
	vfprintf(stderr, fmt, args);
}


/**
 * Display error.
 * @param fmt		Format string.
 * @param ...		Format arguments.
 */
void error(const char *fmt, ...) {
	va_list args;
	va_start(args, fmt);
	error_args(fmt, args);
	va_end(args);
}


/**
 * Display a syntax error.
 * @param prog_name		Program name.
 * @param fmt			Format string.
 * @param ...			Format arguments.
 */
void syntax_error(char *prog_name, const char *fmt, ...) {
	va_list args;
	usage(prog_name);
	va_start(args, fmt);
	error_args(fmt, args);
	va_end(args);
}

extern char **environ;

typedef struct init_options_t {
	uint32_t flags;
#		define FLAG_ALLOCATED_ARGV	0x00000001
	int argc;
	char **argv;
	char **envp;
} init_options;


/**
 * Copy options from simulator to GLISS enviroment.
 * @param env	GLISS options to set.
 * @param opts	Simulator options to get.
 */
void copy_options_to_arm_env(arm_env_t *env, init_options *opt)
{
	env->argc = opt->argc;

	env->argv = opt->argv;
	env->argv_addr = 0;

	env->envp = opt->envp;
	env->envp_addr = 0;

	env->auxv = 0;
	env->auxv_addr = 0;

	env->stack_pointer = 0;
}


/**
 * Free the allocated options.
 * @param opt	Options to free.
 */
void free_options(init_options *opt)
{
	int i = 0;

	/* cleanup argv */
	if(opt->argv && (opt->flags & FLAG_ALLOCATED_ARGV)) {
		for (i = 0; opt->argv[i]; i++)
			free(opt->argv[i]);
		free(opt->argv);
	}

	/* cleanup envp */
	if (opt->envp) {
		for (i = 0; opt->envp[i]; i++)
			free(opt->envp[i]);
		free(opt->envp);
	}
}


/**
 * Prepare options from a given table.
 * @param argc		Argument count.
 * @param argv		Argument list.
 * @param options	Options to initialize.
 */
void make_argv_from_table(int argc, char **argv, init_options *options) {
	options->argv = argv;
	options->argc = argc;
}

/**
 * Generate a string with the registers values
 */
void registers_to_string(char* output, const arm_state_t *state) {
	char str[16];
	output[0]='{';
	output[1]='\0';

	for (int i=0 ; i<16 ; ++i) {
		if (i >= 8 && i != 15)
			sprintf(str, "%d", state->GPR[i+8]);
		else
			sprintf(str, "%d", state->GPR[i]);

		strcat(output, str);
		if (i != 15)
			strcat(output, ",");
	}

	strcat(output, "}");
}

/**
 * Generate a string with the start and size of execution and data stacks
 */
void memory_informations(const char* exe_path, int* start_address, int* end_address) {
	arm_loader_sect_t *s_tab;
	arm_loader_t *loader;
	*start_address = -1;
	*end_address = 0;

	/* we need a loader alone for sections */
	loader = arm_loader_open(exe_path);
	if (loader == NULL) {
		fprintf(stderr, "ERROR: cannot load the given executable : %s.\n", exe_path);
		exit(2);
	}

	s_tab = (arm_loader_sect_t *)malloc(arm_loader_count_sects(loader) * sizeof(arm_loader_sect_t));
	for(int s_it = 0; s_it < arm_loader_count_sects(loader); s_it++)
	{
		arm_loader_sect_t data;
		arm_loader_sect(loader, s_it, &data);
		if(data.type == ARM_LOADER_SECT_TEXT || data.type == ARM_LOADER_SECT_DATA)
		{
			if(*start_address == -1 || *start_address > data.addr)
				*start_address = data.addr;
			if(*end_address < data.addr+data.size-4)
				*end_address = data.addr+data.size-4;
		}
	}
}

int main(int argc, char **argv) {
	arm_state_t *state = 0;
	arm_platform_t *platform = 0;
	arm_loader_t *loader = 0;
	arm_address_t addr_start = 0;
	arm_address_t addr_exit = 0;
	arm_sim_t* sim = NULL;
	char *c_ptr = 0;
	int inst_stat[ARM_INSTRUCTIONS_NB];
	int is_start_given = 0;
	int is_exit_given = 0;
	int prog_index = 0;
	/*Elf32_Sym *Elf_sym = 0;*/
	int sym_exit = 0;
	/* this buffer should be big enough to hold an executable's name + 5 chars */
	char buffer[256];
	char instr_txt[64];
	char registers_txt[128];
	init_options options = {0, 0, 0};
	int i = 0;
	/* socket */
	int sock;
	struct hostent *hostinfo = NULL;
	SOCKADDR_IN sin = { 0 }; /* initialise la structure avec des 0 */

	/* scan arguments */
	for(i = 1; i < argc; i++) {

		/* -h or -help options */
		if(strcmp(argv[i], "-help") == 0 || strcmp(argv[i], "-h") == 0)  {
			usage(argv[0]);
			return 0;
		}

		/* -start= option */
		else if(strncmp(argv[i], "-start=", 7) == 0) {
			is_start_given = i;
			addr_start = strtoul(argv[i] + 7, &c_ptr, 16);
			if(*c_ptr != '\0') {
				syntax_error(argv[0],  "bad start address specified : %s, only hexadecimal address accepted\n", argv[i]);
				return 2;
			}
		}

		/* -exit= option */
		else if(strncmp(argv[i], "-exit=", 6) == 0) {
			is_exit_given = i;
			addr_exit = strtoul(argv[is_exit_given] + 6, &c_ptr, 16);
			if(*c_ptr == '\0') {
				syntax_error(argv[0], "bad exit address specified : %s, only hexadecimal address accepted\n", argv[i]);
				return 2;
			}
		}

		/* option ? */
		else if(argv[i][0] == '-') {
			syntax_error(argv[0], "unknown option: %s\n", argv[i]);
			return 2;
		}

		/* free argument */
		else {
			prog_index = i;
			break;
		}
	}

	/* exec available ? */
	if(prog_index == 0) {
		syntax_error(argv[0], "no executable given !\n");
		return 2;
	}

	/* open the exec file */
	loader = arm_loader_open(argv[prog_index]);
	if(loader == NULL) {
		fprintf(stderr, "ERROR: cannot open program %s\n", argv[prog_index]);
		return 2;
	}

	/* find the _start symbol if no start address is given */
	if(!is_start_given)
		addr_start = arm_loader_start(loader);

	/* find the _exit symbol if no exit address is given */
	if (!is_exit_given) {
		int found = 0;

		/* search symbol _exit */
		for(sym_exit = 0; sym_exit < arm_loader_count_syms(loader); sym_exit++) {
			arm_loader_sym_t data;
			arm_loader_sym(loader, sym_exit, &data);
			if(strcmp(data.name, "_exit") == 0) {
				/* we found _exit */
				addr_exit = data.value;
				found = 1;
				break;
			}
		}

		/* check for error */
		if(!found) {
			syntax_error(argv[0], "ERROR: cannot find the \"_exit\" symbol and no exit address is given.\n");
			return 2;
		}
	}
	options.argv = options.envp = 0;

	/* close loader file */
	arm_loader_close(loader);

	/* make the platform */
	platform = arm_new_platform();
	if (platform == NULL)  {
		fprintf(stderr, "ERROR: cannot create platform\n");
		return 2;
	}

	/* load the image in the platform */
	if (arm_load_platform(platform, argv[prog_index]) == -1) {
		error("ERROR: cannot load the given executable : %s.\n", argv[i]);
		return 2;
	}

	/* make the state depending on the platform */
	state = arm_new_state(platform);
	if (state == NULL)  {
		fprintf(stderr, "ERROR: cannot create state\n");
		return 2;
	}

		/* make the simulator */
	sim = arm_new_sim(state, addr_start, addr_exit);
	if (sim == NULL) {
		fprintf(stderr, "ERROR: cannot create simulator\n");
		return 2;
	}

	fflush(stdout);

	/* Initialize the connexion */
	sock = socket(AF_INET, SOCK_STREAM, 0);
	if(sock == -1) {
		perror("socket()");
		exit(errno);
	}

	hostinfo = gethostbyname("localhost"); 
	if (hostinfo == NULL) {
	    fprintf (stderr, "Unknown host %s.\n", "localhost");
	    exit(-1);
	}

	sin.sin_addr = *(IN_ADDR *) hostinfo->h_addr_list[0];
	sin.sin_port = htons(5321);
	sin.sin_family = AF_INET;

	if(connect(sock,(SOCKADDR *) &sin, sizeof(SOCKADDR)) == -1) {
	    perror("connect()");
	    fprintf(stderr, "c'est bien ici que je plante!\n");
	    exit(errno);
	}

	/* Main loop */
	arm_inst_t *inst;
	char input[64];
	int n, mem_start, mem_end;
	do
	{
		memset(input, 0, 64);
		if((n = recv(sock, input, 64, 0)) > 0) {
			if (0 == strcmp(input, "StepIn")) {
				if (arm_is_sim_ended(sim)) {
					send(sock, "ExecutionFinished;\n", 20, 0);
					continue;
				}

				/* envoi de l'instruction qui vas être executée et envoi des registres */
				inst = arm_next_inst(sim);
				arm_disasm(instr_txt, inst);
				registers_to_string(registers_txt, state);
				sprintf(buffer, "%s\n%s\n%d;\n", instr_txt, registers_txt, state->Ucpsr);
				send(sock, buffer, strlen(buffer), 0);

				/* execution de l'instruction */
				arm_free_inst(inst);
				inst_stat[inst->ident]++;
				arm_step(sim);
			}
			else if (0 == strcmp(input, "GetMemory")) {
				memory_informations(argv[prog_index], &mem_start, &mem_end);
				for (int i = mem_start; i <= mem_end; i+=n) {
					n = (i+1024>mem_end)? mem_end-i+4 : 1024;
					arm_mem_read(state->M, i, buffer, n);
					send(sock, buffer, n, 0);
				}
				send(sock, ";\n", 3, 0);
			}
			else if (0 == strcmp(input, "GetMemoryRange")) {
				memory_informations(argv[prog_index], &mem_start, &mem_end);
				sprintf(buffer, "Begin: %d\nEnd: %d;\n", mem_start, mem_end);
				send(sock, buffer, strlen(buffer), 0);
			}
			else if (0 == strcmp(input, "AbortSim")) {
				printf("Simulator aborted by the server\n");
				arm_delete_sim(sim);
				exit(0);
			}
			else {
				send(sock, "UnknownCommand;\n", 17, 0);
				fprintf(stderr, "Unknown command: %s \nSee the doc on https://github.com/leocances/cacheSim/wiki/%%5BPROTOCOLE%%5D---Communication-entre-Glisse-et-CacheSym\n", input);
			}
		} else {
			perror("recv()");
			arm_delete_sim(sim);
			exit(errno);
		}
	} while (n!=-1);
}
