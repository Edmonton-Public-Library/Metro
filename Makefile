########################################################################
# This make file is meant as a helper to rebuild the metro server for 
# distribution to each participating library.
########################################################################

VERSION=0.1
ARCHIVE=Metro_${VERSION}
CONFIGS=config_templates/*.properties
WIN_SETUP=windows/Output/setup.exe
WIN_DIR=windows/*.exe windows/*.dll
UNIX_DIR=unix/*

update: clean update_unix update_windows
install: clean dist_windows dist_unix

update_unix:
	tar cvf ${ARCHIVE}_EPL.tar dist/*
update_windows:
	cp ${WIN_SETUP} ./setup.e__
	
dist_windows:
	zip -r ${ARCHIVE}.z_ ${CONFIGS} dist/* ${WIN_DIR} logs/Customers
dist_unix:
	tar cvf ${ARCHIVE}.tar ${CONFIGS} dist/* ${UNIX_DIR} logs/Customers

clean:
	-rm ${ARCHIVE}.tar 
	-rm ${ARCHIVE}.z_
	-rm ./setup.e__
