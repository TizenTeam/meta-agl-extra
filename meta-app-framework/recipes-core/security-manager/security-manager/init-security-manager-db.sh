#!/bin/sh

if [ ! -e "/var/db/security-manager" ]; then
	mkdir -p /var/db
	cp -ra /usr/dbspace/ /var/db/security-manager
fi
