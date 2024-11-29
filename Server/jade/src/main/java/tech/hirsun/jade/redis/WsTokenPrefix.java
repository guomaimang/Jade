package tech.hirsun.jade.redis;

public class WsTokenPrefix extends BasePrefix{
    public WsTokenPrefix(String prefix) {
        super(60, prefix);
    }

    public static WsTokenPrefix byUUID = new WsTokenPrefix("uuid");
}
