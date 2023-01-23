package com.altun.fiveten.service;

import com.altun.fiveten.model.User;
import com.altun.fiveten.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private  UserRepository userRepository;

    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }

    public List<User> saveUser() {
        List<User> userList = new ArrayList<>();

        for(int i=0;i<100;i++){
            User user = User.builder().username("okan"+i).email("okanaltun"+i+"@gmail.com").password("123").build();
            this.userRepository.save(user);
            userList.add(user);
        }
        return userList;

    }

    public User getUser(Long id){
        return this.userRepository.findById(id).get();
    }


    public List<User> saveFriend(Long id1, Long id2) {
        User user1 = userRepository.findById(id1).get();
        User user2 = userRepository.findById(id2).get();

        List<User> userList = new ArrayList<>();

        user1.getFriends().add(user2);
        user2.getFriends().add(user1);

        userList.add(user1);
        userList.add(user2);

        userRepository.saveAll(userList);

        return userList;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByEmail(String email){
        return this.userRepository.findByEmail(email);
    }

    public User findByUsername(String username){
        return this.userRepository.findByUsername(username).get();
    }
}
