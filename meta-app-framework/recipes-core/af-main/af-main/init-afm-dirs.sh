#!/bin/sh

if [ ! -e "/var/lib/afm" ]; then
	mkdir -p /var/lib
	cp -ra /usr/share/afm /var/lib
fi

