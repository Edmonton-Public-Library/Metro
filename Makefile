SERVICE=service.sh
C_PATH=test
CLIENT=mecard.MetroClient
OUT=/tmp/metro.out
FLAGS=-cp

stop:
	./${SERVICE} stop
restart:
	./${SERVICE} restart
start:
	./${SERVCIE} start
client:
	javac test/mecard/MetroClient.java
test_it: client
	./${SERVICE} start
	tail ${OUT}
	sleep 5
	java ${FLAGS} ${C_PATH} ${CLIENT}
	./${SERVICE} stop
	tail ${OUT}
	netstat -an | grep 2004
