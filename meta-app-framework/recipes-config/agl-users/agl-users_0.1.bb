inherit allarch useradd

SUMMARY = "AGL Users Seed"
DESCRIPTION = "This is a core framework component that\
 defines how users are managed and who are the default users."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"


SRC_URI = ""


RDEPENDS_${PN}_append_smack = " smack-userspace"
DEPENDS_append_smack = " smack-userspace-native"

ALLOW_EMPTY_${PN} = "1"

USERADD_PACKAGES = "${PN}"

USERADD_PARAM_${PN} = "\
  -g users -d /home/agl-driver -m -K PASS_MAX_DAYS=-1 agl-driver ; \
  -g users -d /home/agl-passenger -m -K PASS_MAX_DAYS=-1 agl-passenger \
"


do_configure() {
    :
}

do_compile() {
    :
}

do_install() {
    :
}


pkg_postinst_${PN}() {
    #!/bin/sh -e

    # avoid to run on host
    [ x"$D" != "x" ] && exit 1

    # Drops password
    passwd -d agl-driver
    passwd -d agl-passenger
}
           
pkg_postinst_${PN}_smack() {
    #!/bin/sh -e

    # avoid to run on host
    [ x"$D" != "x" ] && exit 1

    # Actions to carry out on the device go here
    for x in /etc/skel /home/*
    do
        chsmack -a User::Home $x
    done
    passwd -d agl-driver
    passwd -d agl-passenger
}
           
