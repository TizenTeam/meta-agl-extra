require af-main_${PV}.inc 

inherit nativesdk cmake pkgconfig 

SECTION = "base"

DEPENDS = "nativesdk-openssl nativesdk-libxml2 nativesdk-xmlsec1 nativesdk-libzip nativesdk-json-c"

EXTRA_OECMAKE = "\
	-DUSE_LIBZIP=1 \
	-DUSE_SIMULATION=1 \
	-DUSE_SDK=1 \
	-Dafm_name=${afm_name} \
	-Dafm_confdir=${afm_confdir} \
	-Dafm_datadir=${afm_datadir} \
"

do_install_append() {
    # remove unused .pc file we don't want to package
	rm -rf ${D}/${libdir}
}

PACKAGES = "${PN}-tools ${PN}-tools-dbg"
FILES_${PN}-tools = "${bindir}/wgtpkg-* ${afm_confdir}/*"
FILES_${PN}-tools-dbg = "${bindir}/.debug/wgtpkg-*"

