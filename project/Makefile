all:
	javac -cp backend/models/*.java backend/AuthManager.java backend/*.java shell/*.java

trun:
	java -classpath ".:sqlite-jdbc-3.34.0.jar" shell.TraderShell

arun:
	java -classpath ".:sqlite-jdbc-3.34.0.jar" shell.AdminShell

clean:
	rm backend/*.class
	rm shell/*.class