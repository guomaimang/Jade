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



## GPT usage

- How to use **Masonry Layouts** in Kotlin
  - Use LazyVerticalStaggeredGrid
- 

## Ref

1. https://www.iconfont.cn/collections/detail?spm=a313x.collections_index.i1.d9df05512.2cfe3a81iKXHIn&cid=50028

2. https://developer.android.com/develop/ui/compose/lists?hl=zh-cn

3. https://coil-kt.github.io/coil/

4. Image uses
  <ul>
  	<li>https://www.pixiv.net/artworks/124804697</li>
  	<li>https://www.pixiv.net/artworks/124786446</li>
  	<li>https://www.pixiv.net/artworks/124787340</li>
  	<li>https://www.pixiv.net/artworks/124772493</li>
  	<li>https://www.pixiv.net/artworks/124773752</li>
  	<li>https://www.pixiv.net/artworks/124771636</li>
  	<li>https://www.pixiv.net/artworks/124770956</li>
  	<li>https://www.pixiv.net/artworks/124770389</li>
  	<li>https://www.pixiv.net/artworks/124769897</li>
  	<li>https://www.pixiv.net/artworks/124694679</li>
  </ul>

5. https://blog.csdn.net/qq_36072270/article/details/136851199

6. 

