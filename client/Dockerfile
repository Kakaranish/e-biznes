FROM alpine:latest

RUN apk update && apk add bash nano git npm
RUN npm install -g npm@6.8
RUN hash -r

WORKDIR /home/e-biznes/
RUN git clone https://github.com/Kakaranish/e-biznes.git . && git pull origin master
WORKDIR /home/e-biznes/client
RUN npm i

CMD ["npm", "start"]