package atomicity;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class CASFieldUpdaterDemo {
    static final AtomicIntegerFieldUpdater<User> userUpdater = AtomicIntegerFieldUpdater.newUpdater(User.class, "score");

    public static void main(String[] args) {
        User userA = new User("1", "A", 0);
        System.out.println(userA);
        userUpdater.getAndSet(userA, 100);
        System.out.println(userA);
    }
}
