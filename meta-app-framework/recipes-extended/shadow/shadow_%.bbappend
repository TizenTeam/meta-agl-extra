FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append_class-target = " file://0001-useradd-copy-extended-attributes-of-home.patch "
SRC_URI_append_class-native = " file://0001-useradd-copy-extended-attributes-of-home-native.patch "
