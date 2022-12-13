package com.telegramBots.GuideToBukharaBot.model;

import com.telegramBots.GuideToBukharaBot.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("select c.id FROM User c")
    public List<Long> findAllId();
}
