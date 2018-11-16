rm -rf tests/basic/target/surefire-reports
find tests/basic/src/test/java/org/jboss/hal/testsuite/test -type d | sort | sed 's/tests\/basic\/src\/test\/java\///g;s/\//\./g' | while read pkg;
do 
    mvn test -P chrome,basic,standalone -Dtest=$pkg.*Test
done
