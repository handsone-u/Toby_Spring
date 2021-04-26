package user.proxy;

import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class HelloTargetTest {

    @Test
    public void simpleProxy() {
        Hello hello = (Hello)Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
        assertThat(hello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat(hello.sayHi("Toby"), is("HI TOBY"));
        assertThat(hello.sayThankYou("Toby"), is("THANK YOU TOBY"));
    }

}