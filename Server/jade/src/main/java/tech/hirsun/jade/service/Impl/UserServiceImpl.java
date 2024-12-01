package tech.hirsun.jade.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.jade.pojo.User;
import tech.hirsun.jade.service.UserService;
import tech.hirsun.jade.dao.UserDao;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User login(User user) {
        User dbUser = userDao.getUserByEmail(user.getEmail());

        if (dbUser == null) {
            return null;
        }

        if(dbUser.getPassword().equals(user.getPassword())) {
            return new User(dbUser.getId(), dbUser.getNickname(), dbUser.getEmail(), null);
        }else {
            return null;
        }
    }

    @Override
    public User ssoLogin(String email, String displayName) {
        User dbUser = userDao.getUserByEmail(email);
        if (dbUser != null) {
            return dbUser;
        }

        User draftUser = new User();

        draftUser.setNickname(displayName);
        draftUser.setEmail(email);
        draftUser.setPassword(UUID.randomUUID().toString());
        Integer uid = userDao.insert(draftUser);
        draftUser.setPassword(null);
        draftUser.setId(uid);

        return draftUser;
    }

    @Override
    public User getUserInfo(Integer id) {
        User user = userDao.getUserById(id);
        user.setPassword(null);
        user.setEmail(null);
        return user;
    }
}
