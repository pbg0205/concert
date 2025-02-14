FILE_PATH="../../sample-data/"
ABSOLUTE_PATH=$(realpath $FILE_PATH)

echo ${ABSOLUTE_PATH}

docker cp $ABSOLUTE_PATH docker-mysql-1:/var/lib/mysql-files/sample-data/

