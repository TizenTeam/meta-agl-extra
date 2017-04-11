DISTRO_FEATURES_append = " agl-ota"
OVERRIDES .= ":sota"

IMAGE_INSTALL_append = " ostree"

# live image for OSTree-enabled systems
IMAGE_CLASSES += "image_types_ostree image_types_ota"
IMAGE_FSTYPES += "ostreepush otaimg wic"
WKS_FILE = "sdimage-sota.wks"
do_image_wic[depends] += "${IMAGE_BASENAME}:do_image_otaimg"

# required by wic's partitions.py, but is not pulled properly by bitbake
EXTRA_IMAGEDEPENDS += " dosfstools-native mtools-native"

# No working WKS for Raspberry Pi yet
IMAGE_FSTYPES_remove_raspberrypi3 = "wic"
IMAGE_FSTYPES_remove_raspberrypi2 = "wic"

# Still to be integrared
IMAGE_FSTYPES_remove_cyclone5 = "wic"
IMAGE_FSTYPES_remove_dra7xx-evm = "wic"
IMAGE_FSTYPES_remove_dragonboard-410c = "wic"
IMAGE_FSTYPES_remove_h3ulcb = "wic"
IMAGE_FSTYPES_remove_intel-corei7-64 = "wic"
IMAGE_FSTYPES_remove_joule = "wic"
IMAGE_FSTYPES_remove_qemux86-64 = "wic"
IMAGE_FSTYPES_remove_qemux86 = "wic"

# Please redefine OSTREE_REPO in order to have a persistent OSTree repo
OSTREE_REPO ?= "${DEPLOY_DIR_IMAGE}/ostree_repo"
OSTREE_BRANCHNAME ?= "agl-ota-${MACHINE}"
OSTREE_OSNAME ?= "agl"
OSTREE_INITRAMFS_IMAGE ?= "initramfs-ostree-image"

