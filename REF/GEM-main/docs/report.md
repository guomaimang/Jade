---
date: 2022-12-01
article: true
order: 4
headerDepth: 1
icon: blog
---

# Report

## Introduction

In this Project, we commit to do a group project of 5-people called **"GEM"**. We design and implement a mobile computing application using Android platform, especially **Mobile Social Network**, which uses mobile devices to make friends and share photos. 

On one hand, we have applied our knowledge in the course of Mobile Computing - Related concept of **basic Android application development**, *mobility management*, **location-based services**, and **other security issues**. On the other hand, we practise our programming and software engineering skills using java as well as the **Java** and **Android** development platform.

## Computing Model

### Constrains

Mobile computing application designers need to design appropriate models, given the restrictions and constrains of mobile computing environment. These restrictions have a great impact on the design and structure of mobile computing applications. We, the Mobile computing application designers, need to design appropriate models and architectures to organize applications components and specify their relationships. In our project, we assume the following constrains:

1. Mobile devices are resource-poor, and **only operating system of Android**.
2. Network connectivity is **reliable** in this setting, with **low bandwidth** and **intermittent**.
3. Storage resources on server side is unlimited by adopting third-party service (database).

### Three-tier mobile C/S model

Standard C/S model does not work well in mobile environments for several reasons, for example, no support of continuous network connectivity and powerful client ability, and no mobility support. Thus, we adopt the Client/Agent/Server Model (CAS) to support the mobile application.

For Agent, serving as a deputy of the client on the fixed network. It basically supports two roles:

1. **Communications between** client and server pass through agent.
2. Agent **continuously maintains the client’s presence** on the fixed network.
3. Agents will follow the user's movement but remains unchanged.

In our project, we mainly use NGINX service as the agent for supporting our application, which is a performance-oriented HTTP server. Compared with Apache and lighttpd, it has the advantages of less memory and higher stability. The functionalities of NGINX adopted in our application are summarized as follows:

1. **Exchange messages** and queue messages
2. **Offload** processing operations from client
3. **Handle disconnection** between client and server
4. **Optimize transmission** over wireless link

![1669106075822.png](https://static-file.hjm.red/2022/11/22/642c38428aa59.png)
Figure: Architecture Diagram


We have use its configuration file in our project, which details of the configuration file are attached in `Appendix A`. To support the above funtionalities, Nginx has mainly the following 3 implementations:
1. **Forward proxy:** Multiple clients obtain content from nginx when requesting, so they cannot feel the existence of servers.
2. **Load balancing:** With the continuous growth of business and the continuous increase of users, one service can no longer meet the system requirements. At this time, server clusters appeared. In a server cluster, Nginx can set ***uniformly*** or according to weight the received client requests to all servers in the cluster, to distribute server cluster ***pressure*** and ensure the ***stability*** of client access.
3. **Reverse proxy:** Multiple clients obtain content from Nginx when requesting, and the server cannot feel the existence of the clients. This is also to support the functionality of load balancing.

For service and client side, design and implementation details will be introduced in `Section. Client side design` and` Section. Server side design`.

With Alibaba's Global acceleration, GEM's CDN nodes and Agent nodes are located in **almost every city in the world**.

In our project, the adoption of CAS model has several advantages:
1.  Some of **client functionalities** are shifted to agent, such as reliable HTTP connection control.
2.  Agent can **cache some results** to improve performance.
3.  Multiple user agents in different area, which reduced access latency and enhanced stability of mobile network communications.

## Client Side Design

In this section, we will introduce the overall logic flow and interaction of client and server. Please note that the aim is to provide an *overview* and details and implementation will be introduced in later sections.

### Multi-threaded and Transaction-driven

The Android client is designed with object-oriented and transaction-driven programming in mind. The developer clearly classifies tasks so that UI rendering tasks are done by UI threads and network communication tasks are done by network threads. This implements asynchrony and does not cause unnecessary blocking.

### LiveData with ViewModel

LiveData is a component of Android Jetpack, it has the function of listening to the life cycle to ensure that when the life cycle is active, the observer will be notified of data updates in time. Use LiveData does not need to worry too much about memory leaks, because it will bind LifeCycle (such as Activity) when it is created, when the LifeCycle is destroyed, LiveData will be automatically unbound to it.

ViewModel is a concept in the MVVM pattern, it is a bridge between View (view) and Model (model) communication. viewModel role and the MVP pattern in the Presenter is very similar, but ViewModel is a good solution to the problem of too many MVP interfaces.


### Main Activities

After GEM app is opened, the main activities will happen one by one as follows:

1. **Permission is checked:** Whether user would like application access to certain local files, and there involves 3 types of access check and will be introduced in ```section. Functionalities```.
2. **Set up 4 navigation views** After permission is granted, local application will automatically set up the views and these involves no access to server side and completely automatically finishes in client side.
3. **Sep up 4 bottom buttons (Trend, Map, Upload, Profile):** The 4 bottoms are the key functions in this GEM application and their views need to be set initially so that users are able to interact with them in the following.
4. **Set up database (client side and server side):** On Client side, SQLiteDatabase will be used and on server side, MySQL will be used. In this step, we also assume that server already commences.
          
#### Trend Fragment

After initialization, user has 4 options and Trend View is displayed after user clicks on ***Trend***. 

- **Display Post lists:** View will be created after trend button is clicked and post meta list will be get. To get the post lists on client side, the TrendViewModel on client side will send **HTTP GET** to server, and server will send the **HTTP response** together with the post(JSON) back to client. On server side, records will be **selected by SQL** and server will encapsulate the lists in HTTP response. Finally, posts can be displayed.
- **View Post details:** After user click on one specific post, the post details will be shown, including **user name, picture name, post time, message and location.**

![trend.drawio.png](https://static-file.hjm.red/2022/11/22/5e3f9fd40fa1c.png)
Figure. Trend Flow

#### Map Fragment

After initialization, user has 4 options and Trend View is displayed after user clicks on ***Map***. 

1. **Client side:** Client will set **Javascipt**, set **Dom storage**, set **no cache mode** and send **HTTP request** to get the map information.

2. **Server side:** Server will build the **MapBoxModel** and get its HTML format, and finally encasulates it in the **HTTP response** and send back to client, so that Map can be displayed successful on client side.

![map.drawio _1_.png](https://static-file.hjm.red/2022/11/22/eac28ebbd1bc9.png)
Figure. Map Flow

#### Upload Fragment

After initialization, user has 4 options and Trend View is displayed after user clicks on ***Upload***.

1. **View is created and local database is selected:** After creating **Upload View**, controller on client side will get local user info and check log in, and then check location and update access.
2. **Grant permission, upload photo and transmission to Bit Map:** Dialog will be prompted and user can click on two options for uploading photo:
- **From Album:** Choose photo from album
- **From Camera:** Capture photo now 

Please note that only certain permission is granted such that these above functions are able to work. Android will call two different actions called **ACTION_PICK** and **IMAGE_CAPTURE** to get the result of photo and display them through **BitMapModel**, by transmitting photo into Bit Map.

3. **HTTP Request and HTTP Response and transmission in Base64 String:** After photo is displayed, and click **Upload**, it will be transmited into Base64 string format and encapsulated in HTTP Request and send to server side. Server will reply HTTP Response to show whether upload is successful.

4. **View uploading result:** If server replies **"Post Success!"**, then the post is successfully uploaded, otherwise, error message will be displayed.

![upload.drawio.png](https://static-file.hjm.red/2022/11/22/0db4b5a57dc2b.png)
Figure. Post uploading and transmission

#### Profile Fragment

After initialization, user has 4 options and Trend View is displayed after user clicks on ***Profile***. 

##### Register Flow

User needs to register before posting any post. To register, simply put in the user name and password to register. HTTP request will encapsulate the information and send to server, and server will register this record in **MySQL database**. Error message will occur if network is in bad connection.

##### Log In Flow

User needs to log in after registration, by entering registered user name and password and then clicks "log in". However, if they have not registered yet, server will check that no record and display error message. Details of the implementation will be introduced in ```Section. Functionalities```.

##### View personal profile

After logging in, a personal profile page will be shown with basic profile information.

##### Log out

If user presses **Log out**, then they will enter first page of Profile view and log in again.

![profile.drawio.png](https://static-file.hjm.red/2022/11/22/653f6b8cee6b3.png)
Figure. Profile Flow

### Trend Detail Activity

After main activity, the **TrendDetailActivity** will happen to get lists of post and put them in the Trend View. Basic information of each post includes Location, Time, Message, Poster, and Photo. Client needs to request **HTTP** to Server to get the post stored in **MySQL** and display on client side. Details code implementation will be introduced in later section.

![logic_flow.drawio.png](https://static-file.hjm.red/2022/11/22/e895efe70475f.png)
Figure. Logic flow of Activities

## Details of Basic Functionalities

### Local Access

1.  **Capture photo and record video:** Reject/Allow once also in the background / Allow once and not in the background.

2.  **Read device location information:** Reject/Allow once also in the background / Allow once and not in the background.

3.  **Read Local files and photos:** Reject/Allow once also in the background / Allow once and not in the background.

Please note that Unless the user chooses to allow all the time, each time when the application is reopened, the permission request will pop up. If the permissions are not given, some functions of the application will **not** be used.

### Time Stamp Format Standardization

HKT is uniformly used everywhere, and the format is ```"yyyy-MM-dd hh:mm:ss"```.

For **SQL**, the format is as follows:
``` sql
select str_to_date('08.09.2008 08:09:30', '%m.%d.%Y %h:%i:%s'); -- 2008-08-09 08:09:30
```

For **Java**, the format is as follows:
``` java
Date dNow = new Date( );
SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
System.out.println("Current time is: " + ft.format(dNow));
```

### Data Transmission Protocol

#### Register POST

Client will initiates the **HTTP request**, and the format is as follows:

``` 
POST comp4342.hjm.red:8044/register HTTP/1.1 
Content-Type: application/json;charset=utf-8

{"user": "hanjiaming", 
 "password": "123456",
}

```


Server will send **HTTP response**, and the example format is as follows:
``` json
{
    "statue":"Fail",
    "message":"username exist."
}
```

#### Log In POST

Client will initiates the ***HTTP request***, and the format is as follows:
``` ht
POST comp4342.hjm.red:8044 HTTP/1.1 
Content-Type: application/json;charset=utf-8

{"user": "hanjiaming", 
 "password": "123456",
}
```
Server will send ***HTTP response***, and the format is as follows:
``` ht
{
    "statue":"Fail",
    "message":"No this User",
}
```
The fail reason is that this user is not registed yet.After successful log in, the code will return 0.

#### Trend GET

For getting Trend from server, the **Json** format is:
```json
[
[
    {
        "id": 67,
        "user_id": 20,
        "message": "A tired day however tasty supper!",
        "location": "[114.186066,22.304198]",
        "post_time": "2022-11-22 05:58:26",
        "pic_name": "hanjiaming3@Tue-Nov-22-05:58:25-CST-2022",
        "username": "hanjiaming3"
    },
    {
        "id": 66,
        "user_id": 1,
        "message": "A Good day with my friend!",
        "location": "[114.18640739,22.30454971]",
        "post_time": "2022-11-22 05:38:51",
        "pic_name": "hanjiaming@Tue-Nov-22-05:38:50-CST-2022",
        "username": "HanJiaming"
    },
    ......
]
]
```

#### Upload POST

For **HTTP POST request**, the format is as follows:
``` ht
POST comp4342.hjm.red:8044 HTTP/1.1 
Content-Type: application/json;charset=utf-8

{"user": "hanjiaming", 
 "identifier": "fnr4f34g7bjb4hr", 
 "message":"A rainy Day", 
 "location":"[28,32]",
 "picture": "rf3rfuy845nt7457tcnv4"
}
```

For **HTTP response**, the format is as follows:
``` ht
{
    "statue":"Success",
    "commit":"No error",
}
```

### Volley

**Volley** is an HTTP library that makes networking for Android apps **easier** and most **importantly**, faster. 

We have implemented a previous version of HTTP parser, however, this library offers the following **advantages**, rather than implementing HTTP parser by ourselves:

* **Automatic** scheduling of network requests,
* Multiple **concurrent** network connections,
* Transparent disk and memory response **caching** with standard HTTP cache coherence,
* Support for request ***prioritization**,
* **Debugging and tracing** tools.


Below is part of our implementation by making use of this Volley tool to make our development **faster** and **more** **robust**:

``` java
 StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://comp4342.hjm.red/login",
                new Response.Listener<String>() {},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Networking Error", Toast.LENGTH_SHORT).show();
                    }
                }) 
)                                            
```

## Smart Client design

### ServerPostModel

This **ServerPostModel** Class is to get information from the post retrieved on server side, including fields as follows:
``` java
public class ServerPostMeta {
    private String username;
    private String identifier;
    private String location;
    private String message;
    private String picture;
    
    // methods
}
```

### ClientPostModel

This **ClientPostModel** Class is to build post on client side, including fields as follows:
``` java
public class ClientPostMeta {
    private int id;
    private String username;
    private String message;
    private String post_time;
    private String location;
    private String pic_name;
    
    // methods
}
```

### 5.3 UserInfoModel

This **ClientPostModel** Class is to get user information on client side, including fields as follows:
``` java
public class UserInfo {
    private String username;
    private String password;
    
    // methods
}
```

### Local SQLite database

Most application logic of the **GEM** Client is achieved on server side, however, some **frequently required information** are also stored locally on client side. There are in total two types of information stored in Datebase in the whole system: The **posts** and the **profiles**.

Smart client can store its profile information in its **Local SQLite database** via **DBController**, which is a local database opening and upgrading controller: It uniquely and permenantly stores the local user information, which is a common method similar to **caching implementation**. 

During the use of application, network connection may **frequently checks** user's identity, so that it is convenient & required to store the user information locally.

The identifier is equivalent to a token, and serves as the user's login credentials.

``` java
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create local SQL table
        String table = "create table user(id int,username varchar(13), password varchar(13), identifier varchar(65),locationX varchar(63), locationY varchar(63))";
        db.execSQL(table);
    }
```


## Server Side Design

The server is developed based on the Spring Web framework.

- Building: Maven
- Programming Language: Java (Language Level 17)
- JDK: 17
- Deploy Platform: Linux hssvr03 3.10.0-1160.53.1.el7.x86_64 GNU/Linux
- Package: Executable Jar

![76211669111956_.pic.jpg](https://static-file.hjm.red/2022/11/22/89f49ab15833e.jpg)

- The server program is divided into three layers, which is the controller, DAO, and model.
The controller is implemented by the GEMController in a REST style, which creates RESTful web services. GEMController handles HTTP requests from the client application and returns appropriate response or data. The endpoints are shown as follows.

![WechatIMG7664.png](https://static-file.hjm.red/2022/11/22/45ec59b06dbab.png)

- The DAO (Data Access Objects) is implemented by MySQLRepository. MySQLRepository implements several SQL query and update functions. In details, the server uses "com.mysql:mysql-connector-j:8.0.31" to connect mySQL database and jdbcTemplate for executing SQL statements.

- The model includes three classes. Post and User follow the database design, and are used within the server for objects passing. Response is a designed class that is used for returning 3-state HTTP responses to the client program. The three states are SUCCESS, FAIL, and BAD (for invalid request only).

- During the server developing, the programmer adopts "defensive-programming" style to make the server more robust and safe. That is, malicious requests will not break the whole server. And multi-thread model is embedded inside the Spring framework, so it could handle large amount of requests simultaneously.

## Database Design

The back-end uses mySQL as the data storage.

- userinfo: id(auto-generated by mySQL), username, password, identifier(session cookie), enable(blocked or not)
- post: id(auto-generated by mySQL), user_id(the poster's id), message, location, post_time, pic_name.

![47461669060631_.pic.jpg](https://static-file.hjm.red/2022/11/22/7a62b612f8fa1.jpg)

## Advanced Features & Benefits

### Mobility: Network Transport Optimization

The client converts the image to base64 before sending it to the server. Compressing the file helps to reduce the pressure of network transmission. 

The server side also uses file compression to provide both thumbnail and original image references for the client, reducing traffic.

### Location-based service

GEM uses a converged location service that determines the user's geographic location through a combination of GPS and mobile networks. When the application opens, it will first use network services to obtain geographic location faster, and then use GPS and other satellite services to obtain and update more accurate geographic location periodically.

### Security Management

HTTPS adds a layer of SSL/TLS (Secure Sockets Layer/Transport Layer Security) between traditional HTTP and TCP for encryption and decryption.

The client transmits data to the server using the public key for encryption and the private key for decryption on the server side. This process can achieve secure transmission.

### AI recommendation system of posts (Concept)

We have implemented an AI recommendation algorithm and **rating mechanism** on server side, so that system will be able to push the posts trend according to the **user interest**.

**AI recommendation sorted by rating:** All the posts are sorted according to their rating to this specific user.


``` java
    public List<ClientPostMeta> AI_recommendation(List<ClientPostMeta> PostList,List<ClientPostMeta> UserPostList) { // default to 0
        // Calculate "feature similarity"
        // Sort by "feature similarity"
        Map<Objects, Integer> result = MapUtil.sortByValue(AI_rating(PostList, UserPostList));
        return (List)result.keySet();
    }
```

**Content-based filtering:** The approaches utilize a series of discrete, pre-tagged characteristics of an item in order to recommend additional items with similar properties. In our setting, AI rating is calculated as the **average of similarity score** of the specific **post message** with all the posts message of the specific user.

``` java
    public Map<ClientPostMeta, Integer> AI_rating(List<ClientPostMeta> PostList, List<ClientPostMeta> UserPostList) { // default to 0
        // Calculate rating based on Content-based filtering
        // Based on content similarity average
        Map<ClientPostMeta, Integer> result = new HashMap<ClientPostMeta, Integer>();
        for(ClientPostMeta post: PostList){
            int rating_sum = 0;
            for(ClientPostMeta userPost: UserPostList){
                rating_sum += StringSimilarity.similarity(post.getMessage(), userPost.getMessage());
            }
            result.put(post, rating_sum/UserPostList.size());
        }
        return result;
    }
```

**Levenshtein Edit Distance:** This distance is used for calculating the similary of the two strings, which is calculated between a post message and another post message in our setting.

![1669099607067.png](https://static-file.hjm.red/2022/11/22/f209344509202.png)

Figure. Levenshtein Edit Distance Formula

Although this is only a concept, in the future we will deploy and continuously improve.

## Testing strategies

The development of the GEM client follows Google's instructions. Iterative development of a feature can begin by writing a new test or by adding use cases and assertions to an existing unit test. Tests will initially fail because the feature has not yet been implemented.

![1669117280144.png](https://static-file.hjm.red/2022/11/22/9d027e26b74ae.png)

Figure. Two cycles associated with test-driven iterative development

Our test scenarios include

- Virtual machine testing: occurs during development and debugging.
- Real Equipment Testing: Investigate the operation of the program on different models of cell phones.

In the real device's, developers tried brute force requests and random touch presses to test the stability of the application and discover new problems.
