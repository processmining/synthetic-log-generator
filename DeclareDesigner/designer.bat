@echo off

set CLASSPATH_OLD=%CLASSPATH%
set CLASSPATH=%CLASSPATH%;.\classes
set CLASSPATH=%CLASSPATH%;.\lib\jgraph.jar
set CLASSPATH=%CLASSPATH%;.\lib\ltl2aut.jar
set CLASSPATH=%CLASSPATH%;.\lib\xercesImpl.jar
set CLASSPATH=%CLASSPATH%;.\lib\jep-2.4.0.jar
set CLASSPATH=%CLASSPATH%;.\lib\operational_support.jar
set CLASSPATH=%CLASSPATH%;.\lib\OpenXES.jar
set CLASSPATH=%CLASSPATH%;.\lib\OpenXES-XStream.jar
set CLASSPATH=%CLASSPATH%;.\lib\Spex.jar
set CLASSPATH=%CLASSPATH%;.\lib\xstream-1.3.jar
set CLASSPATH=%CLASSPATH%;.\lib\yawl_service.jar
set CLASSPATH=%CLASSPATH%;.\lib\TableLayout.jar


start javaw -classpath "%CLASSPATH%" -Xmx1G -Dsun.java2d.noddraw=true nl.tue.declare.appl.design.Designer

set CLASSPATH=%CLASSPATH_OLD%
set CLASSPATH_OLD=