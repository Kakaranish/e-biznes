FROM ubuntu:18.04

RUN apt-get update && apt install -y \
gnupg2 \
unzip \
nano \
git \
wget \
curl \
openjdk-8-jdk 

RUN wget https://downloads.lightbend.com/scala/2.12.8/scala-2.12.8.deb
RUN dpkg -i scala-2.12.8.deb
RUN rm scala-2.12.8.deb

RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add
RUN apt-get update
RUN apt-get install sbt

WORKDIR /home/backend
COPY . .
RUN sbt compile