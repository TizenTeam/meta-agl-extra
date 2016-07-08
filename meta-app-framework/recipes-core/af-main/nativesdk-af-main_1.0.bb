require af-main_${PV}.inc 

inherit nativesdk cmake pkgconfig 

SECTION = "base"

DEPENDS = "nativesdk-openssl nativesdk-libxml2 nativesdk-xmlsec1 nativesdk-libzip"

afm_name    = "afm"
afm_confdir = "${sysconfdir}/${afm_name}"
afm_datadir = "${datadir}/${afm_name}"

EXTRA_OECMAKE = "\
	-DUSE_LIBZIP=1 \
	-DUSE_SIMULATION=1 \
	-DUSE_SDK=1 \
	-Dafm_name=${afm_name} \
	-Dafm_confdir=${afm_confdir} \
	-Dafm_datadir=${afm_datadir} \
"

PACKAGES = "${PN}-tools ${PN}-tools-dbg"
FILES_${PN}-tools = "${bindir}/wgtpkg-* ${afm_confdir}/*"
FILES_${PN}-tools-dbg = "${bindir}/.debug/wgtpkg-*"

