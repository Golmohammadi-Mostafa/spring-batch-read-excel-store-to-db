package com.axual.demo.dto.mapper;

import com.axual.demo.dto.PokemonDto;
import com.axual.demo.entity.Pokemon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PokemonMapper extends EntityMapper<PokemonDto, Pokemon>{
}
