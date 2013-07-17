########################################################################
# This make file is meant as a helper to rebuild the metro server for 
# distribution to each participating library.
########################################################################

VERSION=0.1
ARCHIVE=MeCard_${VERSION}

EPL_DIR=EPLconfiguration
STR_DIR=STRconfiguration
STA_DIR=STAconfiguration

all: distepl diststrathcona_ftsask diststalbert

diststrathcona_ftsask:
	zip -r ${ARCHIVE}_STR.zip ${STR_DIR}/* dist/*
	
distepl:
	tar cvf ${ARCHIVE}_EPL.tar ${EPL_DIR}/* dist/*
	
diststalbert:
	zip -r ${ARCHIVE}_STA.zip ${STA_DIR}/* dist/*
	
