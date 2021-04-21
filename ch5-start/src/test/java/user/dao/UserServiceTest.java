package user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import user.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.*;

public class UserServiceTest {
    UserService userService;

    @Before
    public void before() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.userService = applicationContext.getBean("userService", UserService.class);
    }
}