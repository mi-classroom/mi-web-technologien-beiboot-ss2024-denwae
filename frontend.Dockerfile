FROM node:20.15.1 as build
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci
COPY frontend .
RUN npm run build

FROM nginx:latest
COPY --from=build /build /usr/share/nginx/html
EXPOSE 80