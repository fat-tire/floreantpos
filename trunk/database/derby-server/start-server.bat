set CLASSPATH=lib\derby.jar;lib\derbynet.jar

java -cp %CLASSPATH% org.apache.derby.drda.NetworkServerControl start -h 0.0.0.0 -p 51527
pause