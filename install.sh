#! /bin/sh


# set the aliases to launch cacheSim
isPresent=0
CacheSimAlias="alias cacheSim=\"java -jar $1/application/CacheSim/cacheSim.jar\""

if [ -f "$HOME/.bash_aliases" ]; then
	isPresent=`grep -c -i "$CacheSimAlias" < $HOME/.bash_aliases`
fi

if [ $isPresent -eq 0 ]; then
	echo $CacheSimAlias >> $HOME/.bash_aliases
fi

if [ ! -f $HOME/.bashrc ]; then
	touch $HOME/.bashrc
fi

if [ `grep -c "if \[ -f ~/.bash_aliases ];" < $HOME/.bashrc` -eq 0 ]; then
	printf '\nif [ -f ~/.bash_aliases ]; then\n\t. ~/.bash_aliases\nfi\n' >> $HOME/.bashrc
fi


# ajout des commandes arm dans le path
armNonePath="$1/arm_tools/bin"

if [ `grep -c "$armNonePath" < $HOME/.bashrc` -eq 0 ]; then
	echo "export PATH=\$PATH:$armNonePath" >> $HOME/.bashrc
fi

# creation du fichier de config
if [ ! -d $HOME/.cacheSim ]; then
	mkdir $HOME/.cacheSim
fi

if [ ! -f $HOME/.cacheSim/cacheSim.conf ]; then
	touch $HOME/.cacheSim/cacheSim.conf
fi

if [ `grep -c "CACHESIM_PATH=" < $HOME/.cacheSim/cacheSim.conf` -eq 0 ]; then
	echo "CACHESIM_PATH=$1" >> $HOME/.cacheSim/cacheSim.conf
fi

