package user.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import user.dao.UserDao;
import user.domain.Level;
import user.domain.User;

import javax.sql.DataSource;
import java.util.List;


public class UserService implements UserLevelUpgradePolicy{
    private UserDao userDao;
    private PlatformTransactionManager transactionManager;
    private MailSender mailSender;

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;

    public UserService() {};

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin()>= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend()>= MIN_RECCOMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown level: "+ currentLevel);
        }
    }
    public void upgradeLevels() throws Exception{
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) upgradeLevel(user);
            }
            this.transactionManager.commit(status);
        } catch (Exception e){
            this.transactionManager.rollback(status);
            throw e;
        }
    }
    public void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEmail(user);
    }

    public void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgraded");
        mailMessage.setText("You grade " + user.getLevel().name());

        mailSender.send(mailMessage);
    }

    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}