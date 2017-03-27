require af-main_${PV}.inc 

# NOTE: using libcap-native and setcap in install doesn't work
# NOTE: there is no SYSTEMD_USER_SERVICE_...
# NOTE: maybe setting afm_name to agl-framework is cleaner but has implications
# NOTE: there is a hack of security for using groups and dbus (to be checked)
# NOTE: using ZIP programs creates directories with mode 777 (very bad)

inherit cmake pkgconfig useradd systemd
BBCLASSEXTEND = "native"

SECTION = "base"

DEPENDS = "openssl libxml2 xmlsec1 systemd libzip json-c systemd security-manager libcap-native af-binder"
DEPENDS_class-native = "openssl libxml2 xmlsec1 libzip json-c"

EXTRA_OECMAKE_class-native  = "\
	-DUSE_LIBZIP=1 \
	-DUSE_SIMULATION=1 \
	-DUSE_SDK=1 \
	-Dafm_name=${afm_name} \
	-Dafm_confdir=${afm_confdir} \
	-Dafm_datadir=${afm_datadir} \
"

EXTRA_OECMAKE = "\
	-DUSE_LIBZIP=1 \
	-DUSE_SIMULATION=0 \
	-DUSE_SDK=0 \
	-Dafm_name=${afm_name} \
	-Dafm_confdir=${afm_confdir} \
	-Dafm_datadir=${afm_datadir} \
	-Dsystemd_units_root=${systemd_units_root} \
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

# short hacks here
SRC_URI += "\
	file://Hack-to-allow-the-debugging.patch \
"

# tools used to install wgt at first boot
SRC_URI += "\
	file://afm-install \
	file://add-qt-wayland-shell-integration.patch \
"

do_install_append() {
    install -d ${D}${bindir}
    install -d -m 0775 ${D}${systemd_units_root}/{system,user}
    install -d -m 0775 ${D}${systemd_units_root}/{system,user}/default.target.wants
    install -d ${D}${afm_datadir}/{applications,icons}
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        mkdir -p ${D}${sysconfdir}/systemd/{system,user}/default.target.wants
        ln -sf ${systemd_user_unitdir}/afm-user-daemon.service ${D}${sysconfdir}/systemd/user/default.target.wants
    fi
    install -m 0755 ${WORKDIR}/afm-install ${D}${bindir}
}

do_install_append_qemux86-64() {
    sed -i -e '/LD_PRELOAD=\/usr\/lib\/libEGL.so/d' ${D}${systemd_user_unitdir}/afm-user-daemon.service
}

pkg_postinst_${PN}() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        chgrp ${afm_name} $D${systemd_units_root}/{system,user}/{default.target.wants,.}
    fi
    chown ${afm_name}:${afm_name} $D${afm_datadir}/{applications,icons,.}
    setcap cap_mac_override,cap_dac_override=ep $D${bindir}/afm-system-daemon
    setcap cap_mac_override,cap_mac_admin,cap_setgid=ep $D${bindir}/afm-user-daemon
}

pkg_postinst_${PN}_smack() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        chgrp ${afm_name} $D${systemd_units_root}/{system,user}/{default.target.wants,.}
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/{system,user}/{default.target.wants,.}
    fi
    chown ${afm_name}:${afm_name} $D${afm_datadir}/{applications,icons,.}
    chsmack -a 'System::Shared' -t $D${afm_datadir}/{applications,icons,.}
    setcap cap_mac_override,cap_dac_override=ep $D${bindir}/afm-system-daemon
    setcap cap_mac_override,cap_mac_admin,cap_setgid=ep $D${bindir}/afm-user-daemon
}
FILES_${PN} += " ${systemd_units_root} "

PACKAGES =+ "${PN}-binding ${PN}-binding-dbg"
FILES_${PN}-binding = " ${afb_binding_dir}/afm-main-binding.so "
FILES_${PN}-binding-dbg = " ${afb_binding_dir}/.debug/afm-main-binding.so "

PACKAGES =+ "${PN}-tools ${PN}-tools-dbg"
FILES_${PN}-tools = "${bindir}/wgtpkg-*"
FILES_${PN}-tools-dbg = "${bindir}/.debug/wgtpkg-*"

