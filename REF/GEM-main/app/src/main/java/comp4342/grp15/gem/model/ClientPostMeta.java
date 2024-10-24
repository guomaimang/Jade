package comp4342.grp15.gem.model;


public class ClientPostMeta {
    private int id;
    private String username;
    private String message;
    private String post_time;
    private String location;
    private String pic_name;

    public ClientPostMeta(int id, String posterName, String message, String time, String location, String pic_name){
        this.id = id;
        this.username = posterName;
        this.message = message;
        this.post_time = time;
        this.location = location;
        this.pic_name = pic_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPic_name() {
        return pic_name;
    }

    public void setPic_name(String pic_name) {
        this.pic_name = pic_name;
    }


}
