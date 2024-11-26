package tech.hirsun.jade.service.Impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.hirsun.jade.service.AppService;
import tech.hirsun.jade.utils.JwtUtils;

@Service
public class AppServiceImpl implements AppService {

    @Value("${jwt.sign-key}")
    private String signKey;

    @Value("${jwt.expire-period}")
    private Long expirePeriod;

    @Override
    @PostConstruct
    public void initApp() {
        JwtUtils.setSignKey(signKey);
        JwtUtils.setExpirePeriod(expirePeriod);
    }

    @Override
    public void resetAll() {

    }
}
