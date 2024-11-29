package tech.hirsun.jade.redis;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BasePrefix implements KeyPrefix{

    @Getter
    private final int expireSeconds;

    private final String prefix;

    public BasePrefix(int expireSeconds, String prefix){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public BasePrefix(String prefix){
        this(86400,prefix); // 0 means never expire
    }

    public String getPrefix(){
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }

}
