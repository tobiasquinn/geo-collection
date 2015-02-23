FROM clojure:lein-2.5.1
MAINTAINER tobias@tobiasquin.com
# load git
RUN apt-get update
RUN apt-get -y dist-upgrade
RUN apt-get -y install git
# add app user
RUN useradd -d /home/app -m -s /bin/bash app

# clone
RUN su app -c \
"cd /home/app && \
git clone https://github.com/tobiasquinn/geo-collection.git && \
cd geo-collection && \
git checkout master"

WORKDIR /home/app/geo-collection
RUN su app -c "lein uberjar"
#CMD su app -c "java -jar target/geo-collection-0.1.0-standalone.jar"
