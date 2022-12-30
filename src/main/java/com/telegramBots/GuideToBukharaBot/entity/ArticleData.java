package com.telegramBots.GuideToBukharaBot.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "local.data.bukhara")
@Data
public class ArticleData {

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "article_data")
    @Type(type = "text")
    String data;

    @Column(name = "article_address")
    @Type(type = "text")
    String address;

}
