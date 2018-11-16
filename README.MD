```bash
mvn package -DskipTests=true
```

```docker
docker build -t todo-api-img .
```

```docker
docker run --name todo-api -p8080:8080 --link todo-mongodb:todo-mongodb-img -e MONGODB_URI=mongodb://todo-mongodb:27017/todorest -d todo-api-img
```

```docker
docker logs todo-api
```

```docker
docker stop todo-api && docker rm todo-api && docker rmi todo-api-img 
```


```
mvn package -DskipTests=true docker:build

```
```docker
docker run --name todo-api-default -p8080:8080 -d todo-api-default

```
```docker
docker stop todo-api-default && docker rm todo-api-default && docker rmi todo-api-default

```


```
mvn package -DskipTests=true docker:build -Dtodo-profile=docker

```

```docker
docker run --name todo-api-docker -p8080:8080 --link todo-mongodb:todo-mongodb-img -e MONGODB_URI=mongodb://todo-mongodb:27017/todorest -d todo-api-img-docker

```

