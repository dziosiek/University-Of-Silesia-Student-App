package com.api.us.restapi.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Integer> {

    @Query("select e.id from Event e where e.date < CURRENT_DATE")
    List<Integer> selectPastEvents();
}
