# build environment
FROM mysql:5.7
MAINTAINER Adam Debski adam-debski.com

ADD ./accountDB.sql /docker-entrypoint-initdb.d
ADD ./tokenDB.sql /docker-entrypoint-initdb.d
EXPOSE 3306 33060
CMD ["mysqld"]
