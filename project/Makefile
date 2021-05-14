all:
	javac -cp backend/*.java shell/*.java

trun:
	java -classpath ".:sqlite-jdbc-3.34.0.jar" shell.TraderShell

clean:
	rm backend/*.class
	rm shell/*.class