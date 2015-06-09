PROJECT_PATH=$(shell pwd)


all: unpack application/arm_sim/simulator install

unpack:
	tar -xjvf arm_tools/gcc-arm-none-eabi-4_9-2015q1-20150306-linux.tar.bz2 -C arm_tools && mv arm_tools/gcc-arm-none-eabi-4_9-2015q1/* arm_tools/ && rm -rf arm_tools/gcc-arm-none-eabi-4_9-2015q1

application/arm_sim/simulator:
	cd application/arm_sim && make

install:
	./install.sh $(PROJECT_PATH)

clean_src:
	rm -rf arm_tools/gcc-arm-none-eabi-4_9-2015q1-20150306-linux.tar.bz2
	rm -rf application/arm_sim/armv5t
	rm -rf application/arm_sim/gliss2
	rm -rf application/arm_sim/simulator.c
	rm -rf application/arm_sim/simulator.o
	rm -rf application/arm_sim/makefile
	rm -rf application/build
	rm -rf application/nbproject
	rm -rf src
	rm -rf application/build.xml
	rm -rf application/manifest.mf
	rm -rf .git
	rm -rf .gitignore
	rm -rf install.sh
	rm -rf makefile