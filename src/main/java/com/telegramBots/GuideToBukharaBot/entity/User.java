package com.telegramBots.GuideToBukharaBot.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "public.user")
@Getter
@Setter
@ToString
public class User {

    @Id
    @Column(name="chat_id")
    private Long chatId;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="user_name")
    private String userName;

    @Column(name = "status")
    private String status;

    @Column(name="registered_at")
    private Timestamp registeredAt;

}
