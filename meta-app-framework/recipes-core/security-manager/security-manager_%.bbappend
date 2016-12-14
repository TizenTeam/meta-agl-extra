FILESEXTRAPATHS_prepend := "${THISDIR}/security-manager:"

SRC_URI += " file://0001-Adapt-rules-to-AGL.patch \
	     file://init-security-manager-db.service \
	     file://init-security-manager-db.sh"

SYSTEMD_SERVICE_${PN} = "init-security-manager-db.service"

FILES_${PN}_append = "${bindir}/init-security-manager-db.sh"

do_install_append () {
	install -p -D ${WORKDIR}/init-security-manager-db.sh ${D}${bindir}/init-security-manager-db.sh
	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
		install -p -D ${WORKDIR}/init-security-manager-db.service ${D}${systemd_unitdir}/system/init-security-manager-db.service
	fi
}
