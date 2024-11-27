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

# 从编译好的镜像中将jar拷贝到运行时容器，并重命名为app.jar
COPY --from=builder /project/app.jar /app.jar

# 容器启动时执行的命令，这里可加jvm参数
ENTRYPOINT ["java","-jar","/app.jar"]

# 默认使用生产环境配置
CMD ["--spring.profiles.active=prod"]
