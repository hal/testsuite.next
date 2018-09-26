rm -f failed.sh

grep -E 'FAILURE|Exception' tests/basic/target/surefire-reports/*.txt | awk -F ':' '{print $1}' | sort|uniq | while read dir ; do 
    pkg=`echo $dir|sed 's/tests\/basic\/target\/surefire-reports\///g;s/\.txt//g'`; 
    echo "mvn test -P chrome,basic,standalone -Dtest=$pkg" >> failed.sh;
done

cat failed.sh
if [ "$1" = "-r" ] ; then
    rm -f tests/basic/target/surefire-reports/*.txt
    source failed.sh
fi

