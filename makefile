DIR=$(shell pwd)


all: unpack application/arm_sim/simulator install

unpack:
	tar xzvf application/armSim.tar.gz -C application/ -v
	tar xjf  arm_tools/gcc-arm-none-eabi-4_9-2015q1-20150306-linux.tar.bz2 -C arm_tools -v && mv arm_tools/gcc-arm-none-eabi-4_9-2015q1/* arm_tools && rm -rf arm_tools/gcc-arm-none-eabi-4_9-2015q1

application/arm_sim/simulator:
	cd application/arm_sim && make

install:
	./install.sh $(DIR)
