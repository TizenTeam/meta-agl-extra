# This layer contains the WIP feature/demo code only
#---------------------------------------------------
BBLAYERS =+ " \
   ${METADIR}/meta-agl-demo \
"

# These are the dependencies of the AGL DEMO Layer
#-------------------------------------------------
BBLAYERS =+ " \
   ${METADIR}/meta-openembedded/meta-filesystems \
   ${METADIR}/meta-openembedded/meta-ruby \
   ${METADIR}/meta-qt5 \
   ${METADIR}/meta-rust \
"

