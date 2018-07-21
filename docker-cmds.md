
### 컨테이너 상세 정보 보기

```
docker inspect sharp_bartik
```

### 컨테이너 아이디 가져오기 

```
CID=$(docker run -d monolith:1.0.0)
```

### 컨테이너 아이피 가져오기

```
CID=$(docker run -d monolith:1.0.0)
CIP=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' ${CID})
```

### 컨테이너 전부 멈추기

```
stop $(sudo docker ps -aq)
```

### 컨테이너 전부 삭제

```
docker rm $(sudo docker ps -aq)
```

### MySQL

```
docker run -p 3306:3306 --name mysql_boot -e MYSQL_ROOT_PASSWORD=1 -e MYSQL_DATABASE=springboot -e MYSQL_USER=keesun -e MYSQL_PASSWORD=pass -d mysql

docker exec -i -t mysql_boot bash

mysql -u root -p
```
