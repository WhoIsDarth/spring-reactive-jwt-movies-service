# How to run?

## Local

```shell
cd debug
docker-compose up
```

## Production

```shell
docker build -t <image_name> .
docker run -d -p 8080:8080 <image_name>
```