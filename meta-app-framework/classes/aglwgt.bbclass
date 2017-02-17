#
# aglwgt bbclass
#
# Jan-Simon Moeller, jsmoeller@linuxfoundation.org
#
# This class expects a "make package" target in the makefile
# which creates the wgt files in the package/ subfolder.
# The makefile needs to use wgtpkg-pack.
#


# 'wgtpkg-pack' in af-main-native is required.
DEPENDS_append = " af-main-native"

# for bindings  af-binder is required.
DEPENDS_append = " af-binder"

do_aglwgt_package()  {
        cd ${B}
        make package || ( \
          bbwarn "Your makefile must support the 'make package' target" ; \
          bbwarn "and generate a .wgt file using wgtpack in the"; \
          bbwarn "subfolder ./package/ !" ; \
          bbwarn "Fix your package as it will not work within the SDK" ; \
          bbwarn "See: https://wiki.automotivelinux.org/troubleshooting/app-recipes" \
                        )
}

python () {
    d.setVarFlag('do_aglwgt_deploy', 'fakeroot', '1')
}


POST_INSTALL_LEVEL ?= "10"
POST_INSTALL_SCRIPT ?= "${POST_INSTALL_LEVEL}-${PN}.sh"

EXTRA_WGT_POSTINSTALL ?= ""

do_aglwgt_deploy() {
    install -d ${D}/usr/AGL/apps
    install -m 0644 ${B}/package/*.wgt ${D}/usr/AGL/apps/
    APP_FILES=""
    for file in ${D}/usr/AGL/apps/*.wgt;do
        APP_FILES="${APP_FILES} $(basename $file)";
    done
    install -d ${D}/${sysconfdir}/agl-postinsts
    cat > ${D}/${sysconfdir}/agl-postinsts/${POST_INSTALL_SCRIPT} <<EOF
#!/bin/sh -e
for file in ${APP_FILES}; do
    /usr/bin/afm-install install /usr/AGL/apps/\$file
done
sync
${EXTRA_WGT_POSTINSTALL}
EOF
    chmod a+x ${D}/${sysconfdir}/agl-postinsts/${POST_INSTALL_SCRIPT}
}

FILES_${PN} += "/usr/AGL/apps/*.wgt ${sysconfdir}/agl-postinsts/${POST_INSTALL_SCRIPT}"

addtask aglwgt_deploy  before do_package after do_install
addtask aglwgt_package before do_aglwgt_deploy after do_compile
