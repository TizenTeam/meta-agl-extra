require af-main_${PV}.inc 

# NOTE: using libcap-native and setcap in install doesn't work
# NOTE: there is no SYSTEMD_USER_SERVICE_...
# NOTE: maybe setting afm_name to agl-framework is cleaner but has implications
# NOTE: there is a hack of security for using groups and dbus (to be checked)
# NOTE: using ZIP programs creates directories with mode 777 (very bad)

inherit cmake pkgconfig useradd systemd
BBCLASSEXTEND = "native"

SECTION = "base"

DEPENDS = "openssl libxml2 xmlsec1 systemd libzip json-c security-manager libcap-native af-binder"
DEPENDS_class-native = "openssl libxml2 xmlsec1 libzip"

afm_name    = "afm"
afm_confdir = "${sysconfdir}/${afm_name}"
afm_datadir = "/var/lib/${afm_name}"
afm_init_datadir = "${datadir}/${afm_name}"
afb_binding_dir = "${libdir}/afb"

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
	-DUNITDIR_USER=${systemd_user_unitdir} \
	-DUNITDIR_SYSTEM=${systemd_system_unitdir} \
"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "-g ${afm_name} -d ${afm_datadir} -r ${afm_name}"
GROUPADD_PARAM_${PN} = "-r ${afm_name}"

SYSTEMD_SERVICE_${PN} = "afm-system-daemon.service"
SYSTEMD_AUTO_ENABLE = "enable"

SRC_URI_append = "file://init-afm-dirs.sh \
		  ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'file://init-afm-dirs.service', '', d)}"

FILES_${PN} += "\
	${bindir}/init-afm-dirs.sh \
	${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_user_unitdir}/afm-user-daemon.service ${systemd_unitdir}/system/init-afm-dirs.service', '', d)} \
"

RDEPENDS_${PN}_append_smack = " smack-userspace"
DEPENDS_append_smack = " smack-userspace-native"

# short hacks here
SRC_URI += "\
	file://Hack-to-allow-the-debugging.patch \
	file://add-qt-wayland-shell-integration.patch \
"

# tools used to install wgt at first boot
SRC_URI += "\
	file://afm-install \
"

do_install_append() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/init-afm-dirs.sh ${D}${bindir}
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        mkdir -p ${D}${sysconfdir}/systemd/user/default.target.wants
        mkdir -p ${D}${sysconfdir}/systemd/system/default.target.wants
        ln -sf ${systemd_user_unitdir}/afm-user-daemon.service ${D}${sysconfdir}/systemd/user/default.target.wants
	install -p -D ${WORKDIR}/init-afm-dirs.service ${D}${systemd_unitdir}/system/init-afm-dirs.service
	ln -sf ${systemd_unitdir}/system/init-afm-dirs.service ${D}${sysconfdir}/systemd/system/default.target.wants
    fi
    install -m 0755 ${WORKDIR}/afm-install ${D}${bindir}
}

do_install_append_smack () {
    install -d ${D}/${sysconfdir}/smack/accesses.d
    cat > ${D}/${sysconfdir}/smack/accesses.d/default-access-domains-no-user <<EOF
System User::App-Shared rwxat
System User::Home       rwxat
EOF
    chmod 0644 ${D}/${sysconfdir}/smack/accesses.d/default-access-domains-no-user
    install -d ${D}/${sysconfdir}/skel/app-data
    chsmack -a 'User::Home' -t -D ${D}/${sysconfdir}/skel
    chsmack -a 'User::App-Shared' -D ${D}/${sysconfdir}/skel/app-data
}

pkg_postinst_${PN}() {
    mkdir -p $D${afm_init_datadir}/applications $D${afm_init_datadir}/icons
    setcap cap_mac_override,cap_dac_override=ep $D${bindir}/afm-system-daemon
    setcap cap_mac_override,cap_mac_admin,cap_setgid=ep $D${bindir}/afm-user-daemon
}

pkg_postinst_${PN}_smack() {
    mkdir -p $D${afm_init_datadir}/applications $D${afm_init_datadir}/icons
    chown ${afm_name}:${afm_name} $D${afm_init_datadir} $D${afm_init_datadir}/applications $D${afm_init_datadir}/icons
    chsmack -a 'System::Shared' -t $D${afm_init_datadir} $D${afm_init_datadir}/applications $D${afm_init_datadir}/icons
    setcap cap_mac_override,cap_dac_override=ep $D${bindir}/afm-system-daemon
    setcap cap_mac_override,cap_mac_admin,cap_setgid=ep $D${bindir}/afm-user-daemon
}

PACKAGES =+ "${PN}-binding ${PN}-binding-dbg"
FILES_${PN}-binding = " ${afb_binding_dir}/afm-main-binding.so "
FILES_${PN}-binding-dbg = " ${afb_binding_dir}/.debug/afm-main-binding.so "

PACKAGES =+ "${PN}-tools ${PN}-tools-dbg"
FILES_${PN}-tools = "${bindir}/wgtpkg-*"
FILES_${PN}-tools-dbg = "${bindir}/.debug/wgtpkg-*"

