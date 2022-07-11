FROM node:18.5-alpine3.15 as builder
WORKDIR /frontend/sharedPaymentsWeb

COPY sharedPaymentsWeb/*.json /frontend/sharedPaymentsWeb/
COPY sharedPaymentsWeb/*.js /frontend/sharedPaymentsWeb/
COPY sharedPaymentsWeb/src /frontend/sharedPaymentsWeb/src

RUN npm install -g @angular/cli
RUN npm install
RUN ng build -c production

#####################################
FROM nginx:1.23
COPY --from=builder /frontend/sharedPaymentsWeb/dist/shared-payments-web/*.* /usr/share/nginx/html
EXPOSE 80