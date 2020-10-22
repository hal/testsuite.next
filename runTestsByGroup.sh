# search for all packages that contains tests
# and output it as an mvn command to run each packages as
# mvn test -P chrome,basic,standalone -Dtest=org.jboss.hal.testsuite.test.configuration.messaging.*Test

find tests/basic/src/test/java/org/jboss/hal/testsuite/test -type f -name \*Test.java | sed 's/\/[a-Z]*\.java$//g' | sort | uniq \
    | sort | sed 's/tests\/basic\/src\/test\/java\///g;s/\//\./g' | while read -r pkg;
do
    mvn test -P chrome,basic,standalone -Dtest=$pkg.*Test
done
