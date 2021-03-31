package com.eventsweb.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventsweb.challenge.entitites.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

}
