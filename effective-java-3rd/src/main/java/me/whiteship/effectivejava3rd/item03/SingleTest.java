package me.whiteship.effectivejava3rd.item03;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SingleTest {

    public static void main(String[] args) throws NoSuchMethodException {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        UserService userService1 = applicationContext.getBean(UserService.class);
        UserService userService2 = applicationContext.getBean(UserService.class);

        System.out.println(userService1 == userService2);
    }
}
