FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"
SRC_URI += "file://Only-require-libxslt-in-.pc-files-when-necessary.patch"

DEPENDS += "libxml2"

BBCLASSEXTEND = "native nativesdk"
