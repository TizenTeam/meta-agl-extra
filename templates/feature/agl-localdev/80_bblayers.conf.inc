# Local derivations during development, just add a layer in the folder meta-localdev
BBLAYERS =+ "${@'${METADIR}/meta-localdev' if os.path.exists('${METADIR}/meta-localdev') else ''}"

