package com.felixthecat.guideToTukharaBot.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Entity
@Table(name = "start_commands")
@Data
public class StartCommands {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Value("command")
    private String command;

    @Value("start_message_text")
    private String startMessageText;

    @Value("description")
    private String description;
}
