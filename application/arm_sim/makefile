CC=gcc
CFLAGS=-Wall -Wextra -std=c99
CHEMIN= $(shell pwd)/armv5t

all: make_gliss make_armv5t simulator

simulator: simulator.o
	$(CC) $(CFLAGS) -I$(CHEMIN)/include -I$(CHEMIN)/src -o3 $^ -o $@ -L$(CHEMIN)/src -larm

simulator.o: simulator.c
	$(CC) $(CFLAGS) -I$(CHEMIN)/include -I$(CHEMIN)/src -o3 -c $^ -o $@

make_armv5t:
	cd armv5t && make && cd ..

make_gliss:
	cd gliss2 && make && cd ..