package com.felixthecat.guideToTukharaBot.entity;

import lombok.Getter;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Entity
@Table(name = "start_commands_callbacks")
@Getter
public class StartCommandsCallbacks {

    @Id
    @Value("id")
    @Type(type = "int")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Value("user_status")
    private String userStatus;

    @Value("callback_id")
    private int callbackId;
}
