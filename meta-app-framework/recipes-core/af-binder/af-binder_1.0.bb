SUMMARY = "HTTP REST interface to automotive backends for HTML5 UI support"
DESCRIPTION = "Automotive-Framework-Binder Daemon provides a HTTP REST \
interface to various automotive-oriented plugins (sound, radio...), \
allowing HTML5 UIs to send platform-specific requests in a secure way."
HOMEPAGE = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/src/app-framework-binder"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE-2.0.txt;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS = "file json-c libmicrohttpd systemd util-linux"
DEPENDS += "alsa-lib glib-2.0 gssdp gupnp gupnp-av pulseaudio"

SRC_URI_git = "git://gerrit.automotivelinux.org/gerrit/src/app-framework-binder;protocol=https;branch=1.0"
SRC_URI_files = ""
SRC_URI = "${SRC_URI_git} \
           ${SRC_URI_files} \
          "

SRCREV = "897aa6a130eab1eb716fcc13e650fb5833a7ce32"
S = "${WORKDIR}/git"

inherit cmake pkgconfig

FILES_${PN} += "${datadir}"

#############################################
# setup meta package
#############################################
PACKAGES += "${PN}-meta"
ALLOW_EMPTY_${PN}-meta = "1"

#############################################
# setup sample plugin packages
#############################################
PACKAGES_DYNAMIC = "${PN}-plugin-*"

python populate_packages_prepend () {
    afb_libdir = d.expand('${libdir}/afb')
    postinst = d.getVar('plugin_postinst', True)
    pkgs = []
    pkgs_dbg = []

    pkgs += do_split_packages(d, afb_libdir, '(.*)-api\.so$', d.expand('${PN}-plugin-%s'), 'AFB plugin for %s', postinst=postinst, extra_depends=d.expand('${PN}'))
    pkgs += do_split_packages(d, afb_libdir, '(.*(?!-api))\.so$', d.expand('${PN}-plugin-%s'), 'AFB plugin for %s', postinst=postinst, extra_depends=d.expand('${PN}'))

    pkgs_dbg += do_split_packages(d, oe.path.join(afb_libdir, ".debug"), '(.*)-api\.so$', d.expand('${PN}-plugin-%s-dbg'), 'AFB plugin for %s, debug info', postinst=postinst, extra_depends=d.expand('${PN}'))
    pkgs_dbg += do_split_packages(d, oe.path.join(afb_libdir, ".debug"), '(.*(?!-api))\.so$', d.expand('${PN}-plugin-%s-dbg'), 'AFB plugin for %s, debug info', postinst=postinst, extra_depends=d.expand('${PN}'))

    metapkg = d.getVar('PN', True) + '-meta'
    d.setVar('RDEPENDS_' + metapkg, ' '.join(pkgs))
}

#############################################
# setup libafbwsc package
#############################################
PACKAGES =+ "libafbwsc libafbwsc-dev libafbwsc-dbg"

FILES_libafbwsc = "\
	${libdir}/libafbwsc.so.* \
"
FILES_libafbwsc-dev = "\
	${includedir}/afb/afb-wsj1.h \
	${includedir}/afb/afb-ws-client.h \
	${bindir}/afb-client-demo \
	${libdir}/libafbwsc.so \
	${libdir}/pkgconfig/libafbwsc.pc \
"
FILES_libafbwsc-dbg = "\
	${libdir}/.debug/libafbwsc.so.* \
	${bindir}/.debug/afb-client-demo \
"
RDEPENDS_libafbwsc-dbg += "${PN}-dbg libafbwsc-dev"

