package com.axual.demo.repository;

import com.axual.demo.entity.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long>, JpaSpecificationExecutor<Pokemon> {

    @Override
    Page<Pokemon> findAll(Pageable pageable);

    List<Pokemon> findAllByTypeOneOrLegendary(String type, boolean legendary);
}
