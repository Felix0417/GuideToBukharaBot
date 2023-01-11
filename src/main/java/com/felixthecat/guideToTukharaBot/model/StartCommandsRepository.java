package com.felixthecat.guideToTukharaBot.model;


import com.felixthecat.guideToTukharaBot.entity.StartCommands;
import com.felixthecat.guideToTukharaBot.entity.StartCommandsCallbacks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StartCommandsRepository extends CrudRepository<StartCommands, Integer> {

    @Query
    StartCommands getByCommand(String command);

    @Query(value = "select c.* " +
            "from start_commands sc" +
            " left join start_commands_callbacks scc on sc.id = scc.start_commands_id" +
            " left join callbacks c on scc.callback_id = c.id" +
            "where scc.user_status = ?1", nativeQuery = true)
    List<StartCommands> getAllById(String userStatus);
}
