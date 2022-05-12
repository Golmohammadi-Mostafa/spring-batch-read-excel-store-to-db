package com.axual.demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@Entity
public class Pokemon {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "type_1")
    private String typeOne;

    @Column(name = "type_2")
    private String typeTwo;

    @Column(name = "total")
    private Integer total;

    @Column(name = "hp")
    private Integer hp;

    @Column(name = "attack")
    private Integer attack;

    @Column(name = "defense")
    private Integer defense;

    @Column(name = "s_attack")
    private Integer spAttack;

    @Column(name = "s_defense")
    private Integer spDefense;

    @Column(name = "speed")
    private Integer speed;

    @Column(name = "generation")
    private Integer generation;

    @Column(name = "legendary")
    private Boolean legendary;

}
