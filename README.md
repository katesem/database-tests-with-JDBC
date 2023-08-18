# jdbc_connection



### Step 1: Create a Docker Network

```bash
docker network create my-network
```
### Step 2: Run MySQL Container
```bash
docker run --name mysql-container --network my-network -e MYSQL_ROOT_PASSWORD=qweqwe12 -p 3306:3306 -d mysql:latest
```
### Step 3: Run phpMyAdmin Container
```bash

docker run --name phpmyadmin-container --network my-network -d -e PMA_HOST=mysql-container -p 8080:80 phpmyadmin/phpmyadmin
```
### Step 4: Access phpMyAdmin and create Database or create a database with JBDC
