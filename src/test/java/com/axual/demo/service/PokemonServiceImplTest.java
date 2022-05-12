package com.axual.demo.service;

import com.axual.demo.Application;
import com.axual.demo.entity.Pokemon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PokemonServiceImplTest {
    @Autowired
    PokemonService pokemonService;

    @Test
    void search_findGhostOrLegendaryTrue_shouldReturnZero() {
        List<Pokemon> ghost = pokemonService.getPokemonsByTypeAndLegendary("Ghost", true);
        assertEquals(ghost.size(),0);
    }
}