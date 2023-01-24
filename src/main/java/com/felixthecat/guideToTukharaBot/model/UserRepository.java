package com.felixthecat.guideToTukharaBot.model;

import com.felixthecat.guideToTukharaBot.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
