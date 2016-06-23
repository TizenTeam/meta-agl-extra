# NOTE: using libcap-native and setcap in install doesn't work
# NOTE: there is no SYSTEMD_USER_SERVICE_...
# NOTE: maybe setting afm_name to agl-framework is cleaner but has implications
# NOTE: there is a hack of security for using groups and dbus (to be checked)
# NOTE: using ZIP programs creates directories with mode 777 (very bad)

inherit cmake pkgconfig useradd systemd

SUMMARY = "AGL Framework Main part"
DESCRIPTION = "\
This is a core framework component for managing \
applications, widgets, and components. \
"

HOMEPAGE = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/src/app-framework-main"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI_git = "git://gerrit.automotivelinux.org/gerrit/src/app-framework-main;protocol=https;branch=1.0"
SRC_URI_files = ""
SRC_URI = "${SRC_URI_git} \
           ${SRC_URI_files} \
          "

SRCREV = "d0fdae3af6998efdce4c6ba0c5d650898c9c5b27"

SECTION = "base"

S = "${WORKDIR}/git"

DEPENDS = "openssl libxml2 xmlsec1 systemd libzip json-c security-manager libcap-native af-binder"

afm_name    = "afm"
afm_confdir = "${sysconfdir}/${afm_name}"
afm_datadir = "${datadir}/${afm_name}"
afb_plugin_dir = "${libdir}/afb"

EXTRA_OECMAKE = "\
	-DUSE_LIBZIP=1 \
	-DUSE_SIMULATION=0 \
	-Dafm_name=${afm_name} \
	-Dafm_confdir=${afm_confdir} \
	-Dafm_datadir=${afm_datadir} \
	-DUNITDIR_USER=${systemd_user_unitdir} \
	-DUNITDIR_SYSTEM=${systemd_system_unitdir} \
"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "-g ${afm_name} -d ${afm_datadir} -r ${afm_name}"
GROUPADD_PARAM_${PN} = "-r ${afm_name}"

SYSTEMD_SERVICE_${PN} = "afm-system-daemon.service"
SYSTEMD_AUTO_ENABLE = "enable"

FILES_${PN} += "\
	${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_user_unitdir}/afm-user-daemon.service', '', d)} \
"

RDEPENDS_${PN}_append_smack = " smack-userspace"
DEPENDS_append_smack = " smack-userspace-native"

# short hack here
SRC_URI += " file://Hack-to-allow-the-debugging.patch"

do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        mkdir -p ${D}${sysconfdir}/systemd/user/default.target.wants
        ln -sf ${systemd_user_unitdir}/afm-user-daemon.service ${D}${sysconfdir}/systemd/user/default.target.wants
    fi
}

pkg_postinst_${PN}() {
    mkdir -p $D${afm_datadir}/applications $D${afm_datadir}/icons
    setcap cap_mac_override,cap_dac_override=ie $D${bindir}/afm-system-daemon
    setcap cap_mac_override,cap_mac_admin,cap_setgid=ie $D${bindir}/afm-user-daemon
}

pkg_postinst_${PN}_smack() {
    mkdir -p $D${afm_datadir}/applications $D${afm_datadir}/icons
    chown ${afm_name}:${afm_name} $D${afm_datadir} $D${afm_datadir}/applications $D${afm_datadir}/icons
    chsmack -a 'System::Shared' -t $D${afm_datadir} $D${afm_datadir}/applications $D${afm_datadir}/icons
    setcap cap_mac_override,cap_dac_override=ie $D${bindir}/afm-system-daemon
    setcap cap_mac_override,cap_mac_admin,cap_setgid=ie $D${bindir}/afm-user-daemon
}

PACKAGES =+ "${PN}-afbplugin ${PN}-afbplugin-dbg"
FILES_${PN}-afbplugin = " ${afb_plugin_dir}/afm-main-plugin.so "
FILES_${PN}-afbplugin-dbg = " ${afb_plugin_dir}/.debug/afm-main-plugin.so "

PACKAGES =+ "${PN}-tools ${PN}-tools-dbg"
FILES_${PN}-tools = "${bindir}/wgtpkg-*"
FILES_${PN}-tools-dbg = "${bindir}/.debug/wgtpkg-*"

BBCLASSEXTEND = "native nativesdk"

