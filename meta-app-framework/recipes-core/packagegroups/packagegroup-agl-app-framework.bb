SUMMARY = "AGL Application Framework core packages"
DESCRIPTION = "The set of packages required by the AGL Application Framework"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-app-framework \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
	af-binder \
	af-binder-binding-afb-dbus-binding \
	af-binder-binding-authlogin \
	libafbwsc \
	af-main \
	${@bb.utils.contains('DISTRO_FEATURES', 'AGL_WEBRUNTIME', 'web-runtime', '', d)} \
    "
