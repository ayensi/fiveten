package com.altun.fiveten.model;

import com.altun.fiveten.security.SecurityConstants;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.GeoIndexed;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Accessors(chain = true)
@RedisHash(value = "onlineUsers", timeToLive = SecurityConstants.JWT_EXPIRATION)
public class OnlineUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Indexed
    private String username;

    @GeoIndexed
    private Point location;
    public OnlineUser(String username, Point location){
        this.location = location;
        this.username = username;
    }

}
