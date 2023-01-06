package com.altun.fiveten.service;

import com.altun.fiveten.model.OnlineUser;
import com.altun.fiveten.model.User;
import com.altun.fiveten.repository.OnlineRepository;
import com.altun.fiveten.repository.UserRepository;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OnlineService {
    private final OnlineRepository onlineRepository;
    private final UserRepository userRepository;
    private final StringRedisTemplate stringRedisTemplate;
    public OnlineService(OnlineRepository onlineRepository, UserRepository userRepository, StringRedisTemplate stringRedisTemplate) {
        this.onlineRepository = onlineRepository;
        this.userRepository = userRepository;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setOnlineUser(String username, Point location) {

        System.out.println(location);

        OnlineUser user = new OnlineUser(username,location);

        onlineRepository.save(user);

    }

    public List<User> findByLocationNear(Point location, String username) {

        Circle within = new Circle(location, 30000);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().sortAscending().limit(10);

        GeoOperations<String, String> geoOperations = stringRedisTemplate.opsForGeo();
        GeoResults<RedisGeoCommands.GeoLocation<String>> nearbyLocations = geoOperations.radius("onlineUsers:location", within, args);

        List<User> userList = new ArrayList<>();
        nearbyLocations.getContent().forEach((c) -> {
            OnlineUser onlineUser = onlineRepository.findById(c.getContent().getName()).get();

            if(!onlineUser.getUsername().equals(username)){
                userList.add(userRepository.findByUsername(onlineUser.getUsername()).get());
            }

        });



        return userList;
    }

    public OnlineUser getUsersLocationByUsername(String username) {
        return onlineRepository.findByUsername(username).orElse(null);
    }
}
