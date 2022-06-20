########################################################################
# This make file is meant as a helper to rebuild the metro server for 
# distribution to each participating library.
########################################################################

VERSION=1_04_00_a
ARCHIVE=Metro_${VERSION}
CONFIGS=config_templates/*.properties
WIN_SETUP=windows/Output/setup.exe
UNIX_DIR=unix/*

update: clean update_unix 
install: clean dist_windows dist_unix

update_unix:
	tar cvf ${ARCHIVE}.tar dist/*
	
dist_windows:
	# zip -r ${ARCHIVE}.zip ${CONFIGS} ${WIN_SETUP}
	zip ${ARCHIVE}.zip dist/MeCard.jar dist/lib/*.jar windows/prunmgr.exe windows/prunsrv.exe windows/README.txt dist/README.TXT
bare_windows:
	zip ${ARCHIVE}_jar_only.zip dist/MeCard.jar dist/README.TXT windows/README.txt
install_windows:
	zip mecard_Windows.zip dist/MeCard.jar ${CONFIGS} dist/lib/*.jar windows/prunmgr.exe windows/prunsrv.exe windows/README.txt dist/README.TXT

dist_unix:
	tar cvf ${ARCHIVE}.tar ${CONFIGS} dist/* ${UNIX_DIR} logs/Customers

clean:
	-rm ${ARCHIVE}.tar 
	-rm ${ARCHIVE}.zip
