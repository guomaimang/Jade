package tech.hirsun.jade.service;
import tech.hirsun.jade.pojo.User;

import org.springframework.stereotype.Service;

public interface UserService {

    User login(User user);

    User ssoLogin(String email, String displayName);

    User getUserInfo(Integer id);
}