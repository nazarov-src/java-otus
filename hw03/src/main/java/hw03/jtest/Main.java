package hw03.jtest;

import com.sun.jdi.InvocationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            if (args.length == 0) {
                System.out.println("argument missed: <class name to test>");
                return;
            }

            Class clazz = Class.forName(args[0]);
            System.out.println("Testing " + clazz);

            List<Method> testMethods = new LinkedList<>();
            Method before = null, after = null;

            for (Method method : clazz.getMethods()) {

                if (method.getAnnotation(Before.class) != null) {
                    if (before != null)
                        throw new Exception("@Before is already specified for method: " + before.getName());

                    before = method;

                } else if (method.getAnnotation(After.class) != null) {
                    if (after != null)
                        throw new Exception("@After is already specified for method: " + after.getName());

                    after = method;

                } else if (method.getAnnotation(Test.class) != null) {
                    testMethods.add(method);
                }
            }

            for (Method test : testMethods) {

                System.out.println("----- Testing method: " + test.getName() + " ------");

                Object instance = clazz.getConstructor().newInstance();

                if (before != null)
                    before.invoke(instance);

                try {
                    test.invoke(instance);

                } catch (InvocationTargetException e) {

                    System.out.println("Exception thrown: " + e.getCause().getMessage());
                }

                if (after != null)
                    after.invoke(instance);
            }

        } catch (InvocationException e) {
            // got here from before or after methods
            System.out.println("Error from tested object: " + e.getCause().getMessage());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
