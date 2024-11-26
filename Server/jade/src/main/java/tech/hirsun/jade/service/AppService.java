package tech.hirsun.jade.service;

import jakarta.annotation.PostConstruct;

public interface AppService {

    public void initApp();

    public void resetAll();
}
