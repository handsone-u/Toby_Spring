package user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import user.domain.Level;
import user.domain.User;
import user.service.UserLevelUpgradePolicy;
import user.service.UserService;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

public class UserServiceTest {
    UserLevelUpgradePolicy userService;
    UserDao userDao;
    List<User> users;

    @Before
    public void before() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.userService = applicationContext.getBean("userLevelUpgradePolicy", UserLevelUpgradePolicy.class);
        this.userDao = applicationContext.getBean("userDao", UserDao.class);
        users = Arrays.asList(
                new User("bumjin", "Park", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("joytouch", "Kang", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("erwins", "Shin", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD - 1),
                new User("madnite1", "Lee", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
                new User("Green", "Oh", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    @DisplayName("upgrade all qualified users")
    public void upgradeLevels() throws Exception{
        userDao.deleteAll();
        for (User user : users) userDao.add(user);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

    }

    @Test
    @DisplayName("Check if deafault Level")
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(userWithoutLevel.getLevel());
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }


//    static class TestUserServiceException extends RuntimeException{
//    }
//
//    static class TestUserService extends UserService {
//        private String id;
//
//        private TestUserService(String id) {
//            super();
//            this.id = id;
//        }
//        public void upgradeLevel(User user) {
//            if(user.getId().equals(this.id )) throw new TestUserServiceException();
//            super.upgradeLevel(user);
//        }
//    }
//
//    @Test
//    public void upgradeAllOrNothing() {
//        UserService testUserService = new TestUserService(users.get(3).getId());
//        testUserService.setUserDao(this.userDao);
//        userDao.deleteAll();
//
//        for (User user : users) {
//            userDao.add(user);
//        }
//        try {
//            testUserService.upgradeLevels();
//            fail("TestUserServiceException expected");
//        } catch (TestUserServiceException e) {
//        }
//
//        checkLevelUpgraded(users.get(1), false);
//    }
}