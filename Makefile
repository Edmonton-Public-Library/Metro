SERVICE=service.sh
C_PATH=test
CLIENT=mecard.MetroClient
OUT=/tmp/metro
FLAGS=-cp

stop:
	./${SERVICE} stop
	netstat -an | grep 2004
restart:
	./${SERVICE} restart
start:
	./${SERVCIE} start
client:
	javac test/mecard/MetroClient.java
check: error
	tail ${OUT}.out
error:
	tail ${OUT}.err
clean:
	-rm ${OUT}.out
	-rm ${OUT}.err
test_it: client
	./${SERVICE} start
#	-tail ${OUT}.out
	sleep 3
	java ${FLAGS} ${C_PATH} ${CLIENT}
	sleep 2
	./${SERVICE} stop
#	tail ${OUT}.out
#	echo "==="
#	tail ${OUT}.err
	-netstat -an | grep 2004
