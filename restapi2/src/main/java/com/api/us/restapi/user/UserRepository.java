package com.api.us.restapi.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u.id FROM User u where u.login = :login AND u.password = :password")
    public Integer  findUserByLoginAndPassword(@Param("login") String login, @Param("password") String password);
    @Query("SELECT u.id FROM User u where u.login = :login")
    public Integer findUserByLogin(@Param("login") String login);

}
