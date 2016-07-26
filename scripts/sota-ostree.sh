#!/bin/bash

PARAMS=1
TEMPDIR='/tmp/agl'

if [ $# -lt "$PARAMS" ] ; then
	echo "This script needs at least $PARAMS command-line arguments!"
	exit
fi

if [ ! -e $1 ] ; then
	echo "File '$1' does not exist."
	exit
fi

echo "Implementing UsrMove..."

# Mount AGL in /tmp
mkdir -p $TEMPDIR
mount -t ext4 -o loop /media/storage/agl-new/build/tmp/deploy/images/qemux86-64/agl-demo-platform-qemux86-64.ext4 /tmp/agl/

# Modify rootfs according to OSTree requirements
cd $TEMPDIR

# Create ostree directory
mkdir -p sysroot/ostree/
ln -sf sysroot/ostree/ ostree
mkdir -p sysroot/tmp/

# Adjust directory tmp/
rm -rf tmp/
ln -sf sysroot/tmp/ tmp

# Move the traditional etc/ directory to usr/
if [ -e etc/ ] ; then
	mv etc/ usr/
fi

# Move directories to usr/system/
directories="bin sbin lib"
mkdir -p usr/system/
for directory in $directories ; do
	if [ -d ${directory} ] && [ ! -L ${directory} ] ; then
		mv ${directory} usr/system/
		ln -sf usr/system/${directory} ${directory}
        fi
done

# OSTree preserves /var during updates. So directories should be moved to /var and
# symlinks should be created to the new paths
dirvar="home mnt media opt"
for directory in $dirvar ; do
	if [ -d ${directory} ] && [ ! -L ${directory} ] ; then
		mv ${directory} var/
		ln -sf var/${directory} ${dirirectory}
	fi
done

cd ~

echo "Done"

sync
umount $TEMPDIR
