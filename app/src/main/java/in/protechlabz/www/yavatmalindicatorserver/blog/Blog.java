package in.protechlabz.www.yavatmalindicatorserver.blog;

/**
 * Created by Nikesh on 20/03/2017.
 */

public class Blog {

    private String text;
    private String image;
    private String username;
    private String uid;
    private String time;
    private String category;

    public Blog() {
    }

    public Blog(String text, String image, String username, String uid, String time, String category) {
        this.text = text;
        this.image = image;
        this.username = username;
        this.uid = uid;
        this.time = time;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
