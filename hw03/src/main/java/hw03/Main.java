package hw03;

import java.lang.reflect.Constructor;

public class Main {
    public static void main(String[] args) {

        try {
            if (args.length == 0) {
                System.out.println("argument missed: <class name to test>");
                return;
            }

            Object instance = Class.forName(args[0]).getConstructor().newInstance();
            System.out.println(instance);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Main{}";
    }
}
