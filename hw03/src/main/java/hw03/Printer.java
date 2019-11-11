package hw03;

import hw03.jtest.After;
import hw03.jtest.Before;
import hw03.jtest.Test;

public class Printer {

    @Test
    public void Foo() {
        System.out.println("Foo() called");
    }

    @Test
    public void Bar() throws Exception {
        System.out.println("Bar() called");
        throw new Exception("Paper jam");
    }

    @Before
    public void Setup() {
        System.out.println("Setup() called");
    }

    @After
    public void TearDown() {
        System.out.println("TearDown() called");
    }
}
