package comp4342.grp15.gem.model;

public class ServerPostMeta {
    private String username;
    private String identifier;
    private String location;
    private String message;
    private String picture;

    public ServerPostMeta(String username, String identifier, String location, String message, String picture) {
        this.username = username;
        this.identifier = identifier;
        this.location = location;
        this.message = message;
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
