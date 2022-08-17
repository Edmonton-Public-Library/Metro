########################################################################
# This make file is meant as a helper to rebuild the metro server for 
# distribution to each participating library.
########################################################################

VERSION=2.00
ARCHIVE=Metro_${VERSION}
CONFIGS=config_templates/*.properties
UNIX_DIR=unix/*
DIST_FILES=dist/MeCard.jar dist/README.TXT dist/lib/*.jar
WIN_REAMES=windows/README.txt dist/README.TXT README.md
UNIX_READMES=dist/README.TXT README.md

update_windows:
	-rm ${ARCHIVE}_update.zip
	zip ${ARCHIVE}_update.zip ${DIST_FILES} windows/prunmgr.exe windows/prunsrv.exe ${WIN_REAMES}
bare_windows:
	-rm ${ARCHIVE}_jar_only.zip
	zip ${ARCHIVE}_jar_only.zip dist/MeCard.jar ${WIN_REAMES}
install_windows:
	-rm ${ARCHIVE}_install.zip
	zip -r ${ARCHIVE}_install.zip ${DIST_FILES} ${CONFIGS} windows/prunmgr.exe windows/prunsrv.exe ${WIN_REAMES}

dist_unix:
	-rm ${ARCHIVE}.tar
	tar cvf ${ARCHIVE}.tar ${CONFIGS} ${DIST_FILES} ${UNIX_DIR} logs/Customers ${UNIX_READMES}
