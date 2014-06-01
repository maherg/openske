@echo off

set OPENSKE_ROOT=%cd%

set OPENSKE_CONSOLE=%OPENSKE_ROOT%\openske-console

call mvn clean install -DskipTests

call mvn -f %OPENSKE_CONSOLE%\pom.xml exec:java -Dexec.mainClass=openske.console.Console
