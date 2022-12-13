package com.telegramBots.GuideToBukharaBot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "local.data.bukhara")
@Getter
@Setter
public class ArticleData {

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "article_data")
    String data;

}
