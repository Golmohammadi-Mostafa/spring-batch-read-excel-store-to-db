package com.axual.demo.service;

import com.axual.demo.dto.SearchOptionRequest;
import com.axual.demo.entity.Pokemon;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PokemonService {

    void deletePolemons(List<Pokemon> pokemons);

    List<Pokemon> getPokemonsByTypeAndLegendary(String type1, boolean legendary);

    Page<Pokemon> getPokemonByFilters(SearchOptionRequest searchRequest);
}
