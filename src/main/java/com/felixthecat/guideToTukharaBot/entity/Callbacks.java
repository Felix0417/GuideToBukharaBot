package com.felixthecat.guideToTukharaBot.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "callbacks")
@Data
public class Callbacks {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "command")
    private String command;

    @Column(name = "description")
    private String description;
}
