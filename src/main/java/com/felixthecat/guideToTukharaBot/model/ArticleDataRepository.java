package com.felixthecat.guideToTukharaBot.model;

import com.felixthecat.guideToTukharaBot.entity.ArticleData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ArticleDataRepository extends CrudRepository<ArticleData, String> {

    @Query(value = "select ldb.* \n" +
            " from callbacks c \n" +
            " left join callbacks_local_data cld on c.id = cld.callback_id \n" +
            " left join local_data_bukhara ldb on cld.local_data_id = ldb.id \n" +
            " where c.command = ?1", nativeQuery = true)
    ArticleData getArticleDataByCommand(String command);
}
