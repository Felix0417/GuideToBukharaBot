package com.felixthecat.guideToTukharaBot.model;

import com.felixthecat.guideToTukharaBot.entity.ArticleData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ArticleDataRepository extends CrudRepository<ArticleData, String> {

    @Query("select c from ArticleData c where c.id = ?1")
    ArticleData getArticleDataById(String articleId);

    @Query("select url from ArticleData url where url.id = ?1")
    ArticleData getArticleDataByAddress(String articleId);
}
