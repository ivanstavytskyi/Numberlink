FROM node:20-alpine
WORKDIR /app

RUN apk add --no-cache wget

COPY package*.json ./
RUN npm install

COPY . .

EXPOSE 7000

CMD ["sh", "-c", "npm run build && npm run preview -- --host 0.0.0.0 --port 7000"]
