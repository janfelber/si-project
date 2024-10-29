FROM node:18.14.2-alpine3.17 as builder-stage
WORKDIR /usr/src/frontend
COPY ./rocksolid-fe/vue-project/package*.json ./
RUN npm install


FROM node:16.3.0-alpine
WORKDIR /usr/src/frontend
COPY --from=builder-stage /usr/src/frontend/node_modules ./node_modules
COPY ./rocksolid-fe/vue-project .
CMD ["npm", "run", "dev"]