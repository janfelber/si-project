FROM node:18.14.2-alpine3.17 as builder-stage

WORKDIR /usr/src/rocksolid-fe
RUN mkdir /usr/src/rocksolid-fe/node_modules
RUN chmod -R 777 /usr/src/rocksolid-fe/node_modules

COPY ./rocksolid-fe/package*.json ./
RUN npm install


FROM node:16.3.0-alpine
USER node

WORKDIR /usr/src/rocksolid-fe
COPY --chown=node:node --from=builder-stage /usr/src/rocksolid-fe/node_modules ./node_modules
RUN chmod -R 777 /usr/src/rocksolid-fe/node_modules
COPY ./rocksolid-fe .

CMD ["npm", "run", "dev"]