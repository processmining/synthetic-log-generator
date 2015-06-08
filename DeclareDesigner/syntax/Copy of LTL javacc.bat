@echo off

java -classpath "%~f0\..\lib\javacc.jar" javacc -STATIC=false LTL.jj

pause