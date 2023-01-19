package com.felixthecat.guideToTukharaBot.model;


import com.felixthecat.guideToTukharaBot.entity.StartCommands;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface StartCommandsRepository extends CrudRepository<StartCommands, Integer> {

    @Query
    StartCommands getByCommand(String command);
}
