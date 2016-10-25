DISTRO_FEATURES_append = " agl-ota"

IMAGE_INSTALL_append = " ostree"

# live image for OSTree-enabled systems
IMAGE_CLASSES += "image_types_ostree image_types_ota"
IMAGE_FSTYPES += "otaimg"

IMAGE_CLASSES_append_raspberrypi = " sdcard_image-rpi-ota"
IMAGE_CLASSES_append_raspberrypi2 = " sdcard_image-rpi-ota"

IMAGE_CLASSES_remove_raspberrypi = " sdcard_image-rpi-gdp"
IMAGE_CLASSES_remove_raspberrypi2 = " sdcard_image-rpi-gdp"

IMAGE_FSTYPES += "${@'rpi-sdimg-ota' if d.getVar('MACHINE', True) in [ 'raspberrypi', 'raspberrypi2', 'raspberrypi3' ] else ''}"

IMAGE_FSTYPES_remove_raspberrypi = "rpi-sdimg"
IMAGE_FSTYPES_remove_raspberrypi2 = "rpi-sdimg"

UBOOT_MACHINE_qemux86 = "qemu-x86_defconfig"
UBOOT_MACHINE_qemux86-64 = "qemu-x86_defconfig"
UBOOT_MACHINE_raspberrypi3 = "rpi_3_32b_defconfig"

KERNEL_IMAGETYPE_arm = "uImage"

PREFERRED_PROVIDER_virtual/bootloader_qemux86 ?= "u-boot-ota"
PREFERRED_PROVIDER_virtual/bootloader_qemux86-64 ?= "u-boot-ota"
PREFERRED_PROVIDER_virtual/bootloader_raspberrypi3 ?= "u-boot"
PREFERRED_PROVIDER_virtual/bootloader_raspberrypi2 ?= "u-boot"

# Please redefine OSTREE_REPO in order to have a persistent OSTree repo
OSTREE_REPO ?= "${DEPLOY_DIR_IMAGE}/ostree_repo"
OSTREE_BRANCHNAME ?= "agl-ota"
OSTREE_OSNAME ?= "agl"
OSTREE_INITRAMFS_IMAGE ?= "initramfs-ostree-image"

