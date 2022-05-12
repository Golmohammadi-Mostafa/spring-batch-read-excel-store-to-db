package com.axual.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PokemonDto {

    private String code;
    private String name;
    private String typeOne;
    private String typeTwo;
    private Integer total;
    private Integer hp;
    private Integer attack;
    private Integer defense;
    private Integer spAttack;
    private Integer spDefense;
    private Integer speed;
    private Integer generation;
    private Boolean legendary;
}
