# Jade


## Deployment

### Docker

You may import the sql in Server/Database to your database (mysql 8.4.0)

```bash
docker run -p 3127:8080 -d --name jade \
           -e MYSQL_URL="jdbc:mysql://172.17.0.1:3306/jade?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true&allowPublicKeyRetrieval=true" \
           -e MYSQL_USERNAME=jade \
           -e MYSQL_PASSWORD=123456 -e REDIS_HOST=172.17.0.1 \
           -e REDIS_PORT=6379 \
           -e REDIS_PASSWORD=1234  \
           -e REDIS_DATABASE=4  \
           -e AZURE_AD_CLIENT_ID=12345
           -e AZURE_AD_CLIENT_SECRET=23456
hanjiaming/jade:latest
```

Please modify the environment variables according to your environment, if necessary. 
