package tech.hirsun.jade.redis;

public class MessagePrefix extends BasePrefix{
    public MessagePrefix(String prefix) {
        super(432000, prefix);
    }

    public static MessagePrefix message = new MessagePrefix("msg");
}
