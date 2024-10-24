---
date: 2022-09-01
article: true
order: 1
headerDepth: 1

---

# Server Manual

## Environment

- Centos Linux 79
- Macos 10.15.7 +
- Windows 10/11

With JDK 17.0.5+

## Steps

- Centos Linux 7.9 is recommended.
- IntelJ IDEA is recommended since the server use Java language.
- DemoServer is recommended, since it has demoData

1. Download the latest server release from github, or download the demoServer.
   - https://github.com/xyliax/GEM-Server/releases
2. Import the Demo data to your mysql database
3. Prepare Demo data
4. Modify the project configuration file.
   - Edit your Database name and password
   - Edit your Workpath
   - In the end use maven to build
5. `java -jar ./GEM-Server-0.0.1-SNAPSHOT.jar`

If necessary, use NGINX to forward the port to 80. don’t forget to change the server address of the client.

