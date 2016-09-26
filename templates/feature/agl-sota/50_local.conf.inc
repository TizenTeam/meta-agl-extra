DISTRO_FEATURES_append = " agl-ota"

IMAGE_INSTALL_append = " otaimg"

# live image for OSTree-enabled systems
IMAGE_CLASSES += "image_types_ostree image_types_ota"
IMAGE_FSTYPES += "otaimg"

UBOOT_MACHINE_qemux86 = "qemu-x86_defconfig"
UBOOT_MACHINE_qemux86-64 = "qemu-x86_defconfig"

PREFERRED_PROVIDER_virtual/bootloader_qemux86 ?= "u-boot-ota"
PREFERRED_PROVIDER_virtual/bootloader_qemux86-64 ?= "u-boot-ota"

# Please redefine OSTREE_REPO in order to have a persistent OSTree repo
OSTREE_REPO ?= "${DEPLOY_DIR_IMAGE}/ostree_repo"
OSTREE_BRANCHNAME ?= "agl-ota"
OSTREE_OSNAME ?= "agl"

