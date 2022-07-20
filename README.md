mvn install:install-file -DgroupId=org.projectlombok -DartifactId=lombok -Dversion=1.18.26 -Dfile=./lombok-1.18.26.jar -Dpackaging=jar -DgeneratePom=true

sonar-scanner.bat -D"sonar.projectKey=lombok" -D"sonar.java.binaries=build/lombok-main/*" -D"sonar.host.url=http://localhost:9000" -D"sonar.login=sqp_d9403b72313cc390c128e876ccedd30a360c
47d7"