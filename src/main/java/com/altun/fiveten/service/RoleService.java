package com.altun.fiveten.service;

import com.altun.fiveten.enums.ERole;
import com.altun.fiveten.model.Role;
import com.altun.fiveten.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findByName(ERole role) {
        return Optional.of(roleRepository.findByName(role).orElse(null));
    }
}
