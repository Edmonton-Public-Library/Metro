########################################################################
# This make file is meant as a helper to rebuild the metro server for 
# distribution to each participating library.
########################################################################

VERSION=4.01.XX
ARCHIVE=Metro_${VERSION}
CONFIGS=config_templates/*.properties
UNIX_DIR=unix/*
DIST_FILES=dist/MeCard.jar dist/README.TXT dist/lib/*.jar
WIN_READMES=windows/README.txt dist/README.TXT README.md
UNIX_READMES=dist/README.TXT README.md

move2Shared:
	-sudo rm ../Shared/MeCard.jar
	sudo cp dist/MeCard.jar ../Shared/
update_windows:
	-rm ${ARCHIVE}_update.zip
	zip ${ARCHIVE}_update.zip ${DIST_FILES} windows/prunmgr.exe windows/prunsrv.exe ${WIN_READMES}
bare_windows:
	-rm ${ARCHIVE}_jar_only.zip
	zip ${ARCHIVE}_jar_only.zip dist/MeCard.jar ${WIN_READMES}
install_windows:
	-rm ${ARCHIVE}_install.zip
	zip -r ${ARCHIVE}_install.zip ${DIST_FILES} ${CONFIGS} windows/prunmgr.exe windows/prunsrv.exe ${WIN_READMES}

dist_unix:
	-rm ${ARCHIVE}.tar
	tar cvf ${ARCHIVE}.tar ${CONFIGS} ${DIST_FILES} ${UNIX_DIR} logs/Customers ${UNIX_READMES}
	sudo cp ${ARCHIVE}.tar ../Shared/
	
sandbox:
	cp papi.properties.sandbox papi.properties

wbrl:
	cp papi.properties.wbrl papi.properties
