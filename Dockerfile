# 拉取编译环境
FROM maven:3.9.9-eclipse-temurin-22-jammy as builder

# 拷贝源码到固定的目录，注意前面有个 '.'
COPY ./Server/jade /project

# 切换到源码目录
WORKDIR /project

# 使用maven进行编译
RUN mvn clean package -Dmaven.test.skip=true

# 重命名编译出来的jar包为app.jar
RUN mv target/*.jar /project/app.jar

FROM eclipse-temurin:22-jre

# 设置默认的环境变量
ENV PICTURES_PATH=resource
ENV PICTURES_MAX_SIZE=10MB
ENV JWT_SIGN_KEY=NuwhbujHwsvJpwq2peJGkw23ejTmhqoqh2tydkei9izheoo9
ENV JWT_EXPIRE_PERIOD=1209600000
ENV MYSQL_URL=jdbc:mysql://172.17.0.1:3306/jade?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true&allowPublicKeyRetrieval=true
ENV MYSQL_USERNAME=jade
ENV MYSQL_PASSWORD=123456

# 从编译好的镜像中将jar拷贝到运行时容器，并重命名为app.jar
COPY --from=builder /project/app.jar /app.jar

# 容器启动时执行的命令，这里可加jvm参数
ENTRYPOINT ["java","-jar","/app.jar"]

# 默认使用生产环境配置
CMD ["--spring.profiles.active=prod"]
