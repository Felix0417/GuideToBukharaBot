package com.felixthecat.guideToTukharaBot.model;

import com.felixthecat.guideToTukharaBot.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("select c.id FROM User c")
    List<Long> findAllId();
}
