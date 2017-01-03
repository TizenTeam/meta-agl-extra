SUMMARY = "AGL Framework Main part"
DESCRIPTION = "\
This is a core framework component for managing \
applications, widgets, and components. \
"

HOMEPAGE = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/src/app-framework-main"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI_git = "git://gerrit.automotivelinux.org/gerrit/src/app-framework-main;protocol=https;branch=chinook"
SRC_URI_files = ""
SRC_URI = "${SRC_URI_git} \
           ${SRC_URI_files} \
          "

SRCREV = "60adb220dc12a51ff8745d8202fe57d12e6c07d4"

S = "${WORKDIR}/git"

