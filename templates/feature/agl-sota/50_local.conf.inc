DISTRO_FEATURES_append = " agl-ota"

# live image for OSTree-enabled systems
IMAGE_CLASSES += "image_types_ota"
IMAGE_FSTYPES += "otaimg"

UBOOT_MACHINE_qemux86 = "qemu-x86_defconfig"
UBOOT_MACHINE_qemux86-64 = "qemu-x86_defconfig"

PREFERRED_PROVIDER_virtual/bootloader_qemux86 ?= "u-boot-ota"
PREFERRED_PROVIDER_virtual/bootloader_qemux86-64 ?= "u-boot-ota"

