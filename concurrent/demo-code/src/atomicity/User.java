package atomicity;
import java.io.Serializable;

public class  User implements Serializable {
    String uid;
    String name;
    public volatile int score;

    public User(String uid, String name, int score) {
        this.uid = uid;
        this.name = name;
        this.score = score;
    }
    @Override
    public String toString() {
        return "User{" +
            "uid='" + uid + '\'' +
            ", name='" + name + '\'' +
            ", score=" + score +
            '}';
    }
}
