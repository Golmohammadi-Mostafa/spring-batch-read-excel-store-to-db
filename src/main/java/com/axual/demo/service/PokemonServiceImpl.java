package com.axual.demo.service;

import com.axual.demo.dto.SearchOptionRequest;
import com.axual.demo.entity.Pokemon;
import com.axual.demo.repository.PokemonRepository;
import com.axual.demo.specs.CustomSpecification;
import com.axual.demo.specs.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PokemonServiceImpl implements PokemonService {
    private final PokemonRepository repository;

    public PokemonServiceImpl(PokemonRepository repository) {
        this.repository = repository;
    }

    @Override
    public void deletePolemons(List<Pokemon> pokemons) {
        repository.deleteAllInBatch(pokemons);
    }
    @Override
    public List<Pokemon> getPokemonsByTypeAndLegendary(String type1, boolean legendary) {
        log.debug("return all pokemons with type: {} and legendary: {}", type1, legendary);
        return repository.findAllByTypeOneOrLegendary(type1, legendary);
    }

    @Override
    public Page<Pokemon> getPokemonByFilters(SearchOptionRequest searchRequest) {
        Specification<Pokemon> specification = createSpecification(searchRequest);
        PageRequest pageRequest = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        return repository.findAll(specification, pageRequest);
    }

    private Specification<Pokemon> createSpecification(SearchOptionRequest searchRequest) {
        Specification<Pokemon> searchFilter;
        CustomSpecification<Pokemon> transactionSpecification = new CustomSpecification<>();
        Specification<Pokemon> transactionSearchFilter = null;
        if (StringUtils.isNotEmpty(searchRequest.getSearchFilter())) {
            Node rootNode = new RSQLParser().parse(searchRequest.getSearchFilter());
            transactionSearchFilter = rootNode.accept(new CustomRsqlVisitor<>());
        }
        searchFilter = transactionSpecification;
        if (Objects.nonNull(transactionSearchFilter)) {
            searchFilter = transactionSpecification.and(transactionSearchFilter);
        }
        return searchFilter;
    }
}
