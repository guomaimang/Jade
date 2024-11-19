package tech.hirsun.jade.service.Impl;

import org.springframework.stereotype.Service;
import tech.hirsun.jade.pojo.User;
import tech.hirsun.jade.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User login(User user) {
        return null;
    }

    @Override
    public User ssoLogin(String email, String displayName) {
        return null;
    }

    @Override
    public User getUserInfo(Integer id) {
        return null;
    }
}
