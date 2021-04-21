package user.service;

import user.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);

    void upgradeLevel(User user);

    void upgradeLevels();

    void add(User userWithLevel);
}
