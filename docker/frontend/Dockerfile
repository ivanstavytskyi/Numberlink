FROM node:20-alpine
WORKDIR /app

RUN apk add --no-cache wget

COPY package*.json ./
RUN npm install

COPY . .

CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]