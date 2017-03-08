DEPENDS_append_smack = " smack-userspace-native"
RDEPENDS_${PN}_append_smack = " smack-userspace"

do_install_append() {
    install -d ${D}/${sysconfdir}/skel/app-data
    install -d ${D}/${sysconfdir}/skel/.config
}

do_install_append_smack () {
    install -d ${D}/${sysconfdir}/smack/accesses.d
    cat > ${D}/${sysconfdir}/smack/accesses.d/default-access-domains-no-user <<EOF
System User::App-Shared rwxat
System User::Home       rwxat
EOF
    chmod 0644 ${D}/${sysconfdir}/smack/accesses.d/default-access-domains-no-user
}

pkg_postinst_${PN}_append_smack() {
    chsmack -r -a 'User::Home' -t -D $D/${sysconfdir}/skel
    chsmack -a 'User::App-Shared' -D $D/${sysconfdir}/skel/app-data
}

