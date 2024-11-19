package tech.hirsun.jade.redis;

public interface KeyPrefix {
    public int getExpireSeconds();
    public String getPrefix();
}
