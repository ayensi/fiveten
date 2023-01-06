package com.altun.fiveten.controller;

import com.altun.fiveten.model.OnlineUser;
import com.altun.fiveten.model.User;
import com.altun.fiveten.security.JWTGenerator;
import com.altun.fiveten.service.OnlineService;
import com.altun.fiveten.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test")
@Slf4j
@Validated
public class TestController {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private OnlineService onlineService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/saveUser")
    public ResponseEntity<List<User>> saveUser(){
        return new ResponseEntity<>(userService.saveUser(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<User>> getUser(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUser(id).getFriends(), HttpStatus.OK);
    }

    @PostMapping("/addFriend")
    public ResponseEntity<List<User>> addFriend(@RequestParam Long id1, @RequestParam Long id2){
        return new ResponseEntity<>(userService.saveFriend(id1,id2) ,HttpStatus.OK);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<Object> getNearUsers(@RequestHeader("Authorization") String token){
        String username = jwtGenerator.getUsernameFromJWT(token.substring(7));

        OnlineUser user = onlineService.getUsersLocationByUsername(username);
        Point usersLocation = user.getLocation();

        if(usersLocation!=null){
            return new ResponseEntity<>(onlineService.findByLocationNear(usersLocation,username),HttpStatus.OK);
        }
        return null;
    }

}
