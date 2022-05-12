package com.axual.demo.config;

import com.axual.demo.entity.Pokemon;
import org.springframework.batch.item.ItemProcessor;

public class PokemonProcessor implements ItemProcessor<Pokemon, Pokemon> {

    @Override
    public Pokemon process(final Pokemon pokemon) {
        if (pokemon.getTypeTwo().equals("Steel")) {
            Integer hp = pokemon.getHp() * 2;
            pokemon.setHp(hp);
        }
        if (pokemon.getTypeOne().equals("Bug") || pokemon.getTypeTwo().equals("Flying")){
            Integer speed =  pokemon.getSpeed()+ (pokemon.getSpeed()/10);
            pokemon.setSpeed(speed);
        }
        if (pokemon.getName().startsWith("G")){
            int length = pokemon.getName().replace("G", "").length();
            pokemon.setDefense(5*length);
        }

        return pokemon;
    }
}
