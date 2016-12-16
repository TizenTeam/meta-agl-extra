FILESEXTRAPATHS_prepend := "${THISDIR}/security-manager:"

SRC_URI += " file://0001-Adapt-rules-to-AGL.patch \
	     file://init-security-manager-db.service \
	     file://init-security-manager-db.sh"

FILES_${PN}_append = "${bindir}/init-security-manager-db.sh \
		      ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_unitdir}/system/init-security-manager-db.service', '', d)} \
"

do_install_append () {
	install -p -D ${WORKDIR}/init-security-manager-db.sh ${D}${bindir}/init-security-manager-db.sh
	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
		mkdir -p ${D}${systemd_unitdir}/system
		mkdir -p ${D}${sysconfdir}/systemd/system/default.target.wants
		install -p -D ${WORKDIR}/init-security-manager-db.service ${D}${systemd_unitdir}/system/init-security-manager-db.service
		ln -sf ${systemd_unitdir}/system/init-security-manager-db.service ${D}${sysconfdir}/systemd/system/default.target.wants
	fi
}
