package tech.hirsun.jade.controller;

import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.jade.pojo.User;
import tech.hirsun.jade.redis.WsTokenPrefix;
import tech.hirsun.jade.redis.service.RedisBasicService;
import tech.hirsun.jade.result.Result;
import tech.hirsun.jade.result.ErrorCode;
import tech.hirsun.jade.service.UserService;
import tech.hirsun.jade.utils.JwtUtils;
import tech.hirsun.jade.controller.exception.custom.*;
import java.util.UUID;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/userauth")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisBasicService redisBasicService;

    // Login API
    @PostMapping("/login")
    public Result login(@RequestBody User userAttempted){
        log.info("User login api is requested, username: {}", userAttempted.getEmail());
        User u = userService.login(userAttempted);

        // If user exists, create a JWT and return it
        if(u != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("email", u.getEmail());
            String jwt = JwtUtils.createJwt(claims);
            Map<String, Object> map = new HashMap<>();
            map.put("jwt", jwt);
            map.put("user", u);
            return Result.success(map);
        }else {
            // If user does not exist, return error message
            throw new BadRequestException("User does not exist.", ErrorCode.USER_NOT_EXIST);
        }
    }

    // Refresh token API
    @PutMapping("/refresh_token")
    public Result refreshToken(@RequestBody Map<String, String> map){

        try {
            String oldJwt = map.get("jwt");
            log.info("User refresh token api is requested, old jwt: {}", oldJwt);

            // if the jwt is null, reject the request
            if(StringUtils.isNullOrEmpty(oldJwt)){
                throw new BadRequestException("The request header jwt is null, return not logged in information", ErrorCode.USER_NOT_LOGIN);
            }

            // parse the jwt, if the jwt is invalid, return false
            Map<String, Object> oldClaims = JwtUtils.parseJwt(oldJwt);

            if(Long.parseLong(oldClaims.get("exp").toString()) * 1000 - new Date().getTime() > 1000 * 60 * 60 * 6 ){
                throw new BadRequestException("No need to refresh the token", ErrorCode.USER_NOT_LOGIN);
            }else{
                Map<String, Object> newClaims = new HashMap<>();
                newClaims.put("id", oldClaims.get("id"));
                newClaims.put("email", oldClaims.get("email"));
                String jwt = JwtUtils.createJwt(newClaims);
                log.info("Jwt refreshed");
                return Result.success(jwt);
            }

        }catch (Exception e){
            throw new BadRequestException("The request header jwt is invalid, return not logged in information", ErrorCode.USER_NOT_LOGIN);
        }
    }

    // Websocket Token
    @GetMapping("/gen_ws_token")
    public Result generateWsToken(@RequestHeader String jwt){
        try {
            log.info("User websocket token api is requested, jwt: {}", jwt);
            // if the jwt is null, reject the request
            if(StringUtils.isNullOrEmpty(jwt)){
                throw new BadRequestException("The request header jwt is null, return not logged in information", ErrorCode.USER_NOT_LOGIN);
            }

            // parse the jwt, if the jwt is invalid, return false
            Map<String, Object> oldClaims = JwtUtils.parseJwt(jwt);
            String id = oldClaims.get("id").toString();
            UUID uuid = UUID.randomUUID();
            redisBasicService.set(WsTokenPrefix.byUUID, uuid.toString(), id);

            return Result.success(uuid);

        }catch (Exception e){
            throw new BadRequestException("The request header jwt is invalid, return not logged in information", ErrorCode.USER_NOT_LOGIN);
        }
    }

}
