package atomicity;

import java.util.concurrent.atomic.AtomicReference;

public class CASReferenceDemo {
    static AtomicReference<User> champion = new AtomicReference<>();
    public static void main(String[] args) {
        User userA = new User("1", "A", 80);
        User userB = new User("2", "B", 100);
        champion.set(userA);
        System.out.println(champion.get());
        champion.set(userB);
        System.out.println(champion.get());
    }
}

