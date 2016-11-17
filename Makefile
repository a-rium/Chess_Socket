all:
	javac client/*.java
	javac server/*.java
	javac shared/*.java

runc:
	java client.Main

runs:
	java server.Main