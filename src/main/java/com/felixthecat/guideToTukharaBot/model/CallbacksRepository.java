package com.felixthecat.guideToTukharaBot.model;

import com.felixthecat.guideToTukharaBot.entity.Callbacks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CallbacksRepository extends CrudRepository<Callbacks, Integer> {

    @Query(value = "select c.*" +
            " from start_commands sc" +
            " left join start_commands_callbacks scc on sc.id = scc.start_commands_id" +
            " left join callbacks c on scc.callback_id = c.id " +
            " where scc.user_status = ?1", nativeQuery = true)
    List<Callbacks> getAllByUserStatus(String userStatus);

    @Query(value = "select c.*" +
            " from callbacks_to_callbacks ctc\n" +
            " left join callbacks c on ctc.to_id = c.id\n" +
            " where ctc.type_button = ?1", nativeQuery = true)
    List<Callbacks> getAllByButtonType(String type);

}
