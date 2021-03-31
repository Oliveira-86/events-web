package com.eventsweb.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventsweb.challenge.entitites.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
