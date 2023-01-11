package com.felixthecat.guideToTukharaBot.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "local_data_bukhara")
@Data
public class ArticleData {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "article_data")
    @Type(type = "text")
    private String data;

    @Column(name = "article_address")
    @Type(type = "text")
    private String address;

}
