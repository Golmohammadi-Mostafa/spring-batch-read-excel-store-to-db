package com.axual.demo.config;

import com.axual.demo.entity.Pokemon;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@Component
public class PokemonFieldSetMapper implements FieldSetMapper<Pokemon> {

    @Override
    public Pokemon mapFieldSet(FieldSet fieldSet) {
        final Pokemon pokemon = new Pokemon();
        pokemon.setCode(fieldSet.readRawString("code"));
        pokemon.setName(fieldSet.readRawString("name"));
        pokemon.setTypeOne(fieldSet.readRawString("type_1"));
        pokemon.setTypeTwo(fieldSet.readRawString("type_2"));
        pokemon.setTotal(fieldSet.readInt("total"));
        pokemon.setHp(fieldSet.readInt("hp"));
        pokemon.setAttack(fieldSet.readInt("attack"));
        pokemon.setDefense(fieldSet.readInt("defense"));
        pokemon.setSpAttack(fieldSet.readInt("s_attack"));
        pokemon.setSpDefense(fieldSet.readInt("s_defense"));
        pokemon.setSpeed(fieldSet.readInt("speed"));
        pokemon.setGeneration(fieldSet.readInt("generation"));
        pokemon.setLegendary(fieldSet.readBoolean("legendary"));

        return pokemon;

    }
}
