package com.altun.fiveten.repository;

import com.altun.fiveten.model.OnlineUser;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OnlineRepository extends CrudRepository<OnlineUser, String> {
    Optional<OnlineUser> findByUsername(String username);
}
