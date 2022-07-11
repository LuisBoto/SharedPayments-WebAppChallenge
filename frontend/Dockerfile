FROM node:18.5-alpine3.15 as builder
WORKDIR /frontend/sharedPaymentsWeb

COPY sharedPaymentsWeb/*.json /frontend/sharedPaymentsWeb/
COPY sharedPaymentsWeb/*.js /frontend/sharedPaymentsWeb/
COPY sharedPaymentsWeb/src /frontend/sharedPaymentsWeb/src

RUN npm install -g @angular/cli
RUN npm install
RUN ng build

FROM nginx:1.23
# Copy build from building container
COPY --from=builder /frontend/sharedPaymentsWeb/dist/shared-payments-web/*.* /usr/share/nginx/html
EXPOSE 80