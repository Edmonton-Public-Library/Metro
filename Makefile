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
	zip -r ${ARCHIVE}_STR.z_ dist/* 
updatesta:
	zip -r ${ARCHIVE}_STA.z_ dist/* 

diststr:
	zip -r ${ARCHIVE}_STR.z_ ${STR_DIR}/* dist/* ${WIN_DIR}/* logs/ config_templates/
distepl:
	tar cvf ${ARCHIVE}_EPL.tar ${EPL_DIR}/* dist/* logs/ unix/* config_templates/
diststa:
	zip -r ${ARCHIVE}_STA.z_ ${STA_DIR}/* dist/* ${WIN_DIR}/* logs/ config_templates/
clean:
	-rm ${ARCHIVE}_EPL.tar 
	-rm ${ARCHIVE}_STR.z_
	-rm ${ARCHIVE}_STA.z_
