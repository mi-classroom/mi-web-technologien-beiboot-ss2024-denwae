FROM node:20.15.1 as build
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci
COPY frontend .
RUN npm run build

FROM nginx:latest
COPY nginx/nginx.conf /etc/nginx/templates/nginx.conf.template
COPY --from=build /build /usr/share/nginx/html
EXPOSE 80