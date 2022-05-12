package com.axual.demo.rest;

import com.axual.demo.dto.PokemonDto;
import com.axual.demo.dto.SearchOptionRequest;
import com.axual.demo.dto.mapper.PokemonMapper;
import com.axual.demo.entity.Pokemon;
import com.axual.demo.service.PokemonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class PokemonResource {

    private final PokemonMapper pokemonMapper;
    private final PokemonService pokemonService;

    public PokemonResource(PokemonMapper pokemonMapper, PokemonService pokemonService) {
        this.pokemonMapper = pokemonMapper;
        this.pokemonService = pokemonService;
    }
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("PokemonJob")
    Job processJob;

    @GetMapping("/invoke-job")
    public String handle() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(processJob, jobParameters);

        return "Batch job has been invoked";
    }
    @GetMapping("/pokemons")
    public ResponseEntity<List<PokemonDto>> getAllStocks(SearchOptionRequest searchOptionRequest) {
        Page<Pokemon> pokemonByFilters = pokemonService.getPokemonByFilters(searchOptionRequest);
        List<PokemonDto> pokemonDtos = pokemonMapper.toDto(pokemonByFilters.getContent());
        return ResponseEntity.ok().body(pokemonDtos);
    }
}