# enable security features (smack, cynara) - required by Application Framework
OVERRIDES .= ":smack"
DISTRO_FEATURES_append = " smack dbus-cynara"

# use tar-native to support SMACK extended attributes independently of host config
IMAGE_CMD_TAR = "tar --xattrs-include='*'"
IMAGE_DEPENDS_tar_append = " tar-replacement-native"
EXTRANATIVEPATH += "tar-native"

# security: enable ssh server in place of dropbear to support PAM on user sessions
IMAGE_FEATURES += "ssh-server-openssh"

