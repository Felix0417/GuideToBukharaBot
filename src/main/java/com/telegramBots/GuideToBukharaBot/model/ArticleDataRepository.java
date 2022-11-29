package com.telegramBots.GuideToBukharaBot.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ArticleDataRepository extends CrudRepository<ArticleData, String> {

    @Query("select c from ArticleData c where c.id = ?1")
    ArticleData getArticleDataById(String articleId);
}
