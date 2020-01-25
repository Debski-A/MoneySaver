# Introduction
Web app build on Spring Cloud technologies. Simple frontend written in React.js 
STILL UNDER DEVELOPMENT
# Prerequisites
1. Java 11
2. Maven
3. Docker CLI
4. NPM
# How to run 
1. Clone repo
2. Go to java-services folder and type mvn clean install -U
3. Export 3 variables:
- export ACCOUNT_SERVICE_PASSWORD=pass
- export CALCULATION_SERVICE_PASSWORD=pass
- export MYSQLDB_ROOT_PASSWORD=pass
4. Go back to MoneySaver folder and type sudo -E docker-compose up --build -d
5. Go to react-frontend/money-saver-ui and type sudo npm install
6. Type npm start
7. Visit localhost:3000 on Web browser
