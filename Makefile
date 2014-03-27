########################################################################
# This make file is meant as a helper to rebuild the metro server for 
# distribution to each participating library.
########################################################################

VERSION=0.8.13_00
ARCHIVE=Metro_${VERSION}
CONFIGS=config_templates/*.properties
WIN_SETUP=windows/Output/setup.exe
UNIX_DIR=unix/*

update: clean update_unix 
install: clean dist_windows dist_unix

update_unix:
	tar cvf ${ARCHIVE}.tar dist/*
	
dist_windows:
	zip -r ${ARCHIVE}.zip ${CONFIGS} ${WIN_SETUP}
dist_unix:
	tar cvf ${ARCHIVE}.tar ${CONFIGS} dist/* ${UNIX_DIR} logs/Customers

clean:
	-rm ${ARCHIVE}.tar 
	-rm ${ARCHIVE}.zip
	-rm ./MeCard.jar
