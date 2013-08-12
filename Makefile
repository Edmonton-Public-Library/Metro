########################################################################
# This make file is meant as a helper to rebuild the metro server for 
# distribution to each participating library.
########################################################################

VERSION=0.1
ARCHIVE=MeCard_${VERSION}
JAR=MeCard.jar
EPL_DIR=EPLconfiguration
STR_DIR=STRconfiguration
STA_DIR=STAconfiguration
WIN_DIR=windows

update: clean updateepl updatestr updatesta
install: clean distepl diststr diststa

updateepl:
	tar cvf ${ARCHIVE}_EPL.tar dist/*
updatestr:
	zip -r ${ARCHIVE}_STR.zip dist/* 
updatesta:
	zip -r ${ARCHIVE}_STA.z_ dist/* 

diststr:
	zip -r ${ARCHIVE}_STR.zip ${STR_DIR}/* dist/* ${WIN_DIR}/* logs/
distepl:
	tar cvf ${ARCHIVE}_EPL.tar ${EPL_DIR}/* dist/* logs/
diststa:
	zip -r ${ARCHIVE}_STA.z_ ${STA_DIR}/* dist/* ${WIN_DIR}/* logs/
clean:
	-rm ${ARCHIVE}_EPL.tar 
	-rm ${ARCHIVE}_STR.zip
	-rm ${ARCHIVE}_STA.z_
