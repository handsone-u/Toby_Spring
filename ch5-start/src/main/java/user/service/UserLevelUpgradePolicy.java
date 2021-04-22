package user.service;

import org.springframework.mail.MailSender;
import user.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);

    void upgradeLevel(User user);

    void upgradeLevels() throws Exception;

    void add(User userWithLevel);

    void setMailSender(MailSender mailSender);
}
