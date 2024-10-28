package comp4342.group15.gemserver.config;

import comp4342.group15.gemserver.dao.PostMapper;
import comp4342.group15.gemserver.dao.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    UserMapper<Object> userMapper() {
        return new UserMapper<>();
    }

    @Bean
    PostMapper<Object> postMapper() {
        return new PostMapper<>();
    }
}
