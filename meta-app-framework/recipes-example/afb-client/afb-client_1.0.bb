SUMMARY = "HTML5 demo template for AFB"
DESCRIPTION = "afb-client is a sample AngularJS/HTML5 application using \
Application Framework Binder with token binding."
HOMEPAGE = "http://www.iot.bzh"

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6cb04bdb88e11107e3af4d8e3f301be5"

#DEPENDS = "nodejs-native"
RDEPENDS_${PN} = "af-binder af-binder-binding-authlogin"

SRC_URI_git = "git://gerrit.automotivelinux.org/gerrit/src/app-framework-demo;protocol=https;branch=master"
SRC_URI_files = "file://afb-client \
          "
SRC_URI = "${SRC_URI_git} \
           ${SRC_URI_files} \
          "
SRCREV = "9e9b459fa27d7a359a060024c9639b99b45813d5"
S = "${WORKDIR}/git/afb-client"

do_install () {
  mkdir -p ${D}/${datadir}/agl/afb-client
  cp -ra ${S}/dist.prod/* ${D}/${datadir}/agl/afb-client/

  mkdir -p ${D}/${bindir}
  install -m 0755 ${WORKDIR}/afb-client ${D}/${bindir}/afb-client
}

FILES_${PN} += "${datadir}"
