package com.telegramBots.GuideToBukharaBot.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ArticleDataRepository extends CrudRepository<ArticleData, String> {

    @Query("select c from ArticleData c where c.id = ?1")
    ArticleData getArticleDataById(String articleId);
}
