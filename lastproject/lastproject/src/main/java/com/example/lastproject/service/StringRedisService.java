package com.example.lastproject.service;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StringRedisService {

    private StringRedisTemplate redisTemplate;
    public StringRedisService(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    @PostConstruct
    public void init() {
       try{ this.redisTemplate.getConnectionFactory()
                .getConnection()
                .flushAll();
    } catch (Exception e) {
        System.err.println("Redis is not available: " + e.getMessage());
    }
    }
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteValue(String key) {redisTemplate.delete(key);}
    public void addUserToList(String listName,String userName){
        redisTemplate.opsForList().leftPush(listName,userName);
    }
    public List<String> getOnlineUsers(String listName){
        return redisTemplate.opsForList().range(listName, 0, -1);
    }
    public void deleteUserFromList(String listName,String username){
         redisTemplate.opsForList().remove(listName,0,username);
    }
}
