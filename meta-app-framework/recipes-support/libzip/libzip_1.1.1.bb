inherit autotools

SUMMARY = "Library providing support for handling zip files"
DESCRIPTION = "\
 This library is wrapping zlib and allows \
 to easily create, browse, inflate of deflate \
 the zip files. \
 It also provides tools for zip comparing, merging or browsing.\
"

HOMEPAGE = "http://nih.at/libzip/index.html"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=23ebf7ca347ed9703b4ef40824d0ef66"

SRC_URI = "http://nih.at/libzip/libzip-1.1.1.tar.xz;md5sum=0c86a1a94fbc3ec6724801036726ae1f"

#SRC_URI = "hg://hg.nih.at/libzip;module=libzip;protocol=http"
#SRCREV = "5895e34af7f9"
#S = "${HGDIR}"

SECTION = "base"

DEPENDS = "zlib"

RDEPENDS_${PN} = "zlib"

PROVIDES += "${PN}-tools"
RDEPENDS_${PN}-tools = "${PN}"
FILES_${PN}-tools = "${bindir}/zipcmp ${bindir}/zipmerge ${bindir}/ziptool"

BBCLASSEXTEND = "native nativesdk"

