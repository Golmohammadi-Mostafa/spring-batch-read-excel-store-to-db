package com.axual.demo.config;

import com.axual.demo.entity.Pokemon;
import com.axual.demo.service.PokemonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationListener extends JobExecutionListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationListener.class);
    private final PokemonService pokemonService;

    @Autowired
    public NotificationListener(PokemonService pokemonService) {
        this.pokemonService = pokemonService;

    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            List<Pokemon> ghost = pokemonService.getPokemonsByTypeAndLegendary("Ghost", true);
            pokemonService.deletePolemons(ghost);
            LOGGER.info("!!! JOB FINISHED! Time to verify the results");
        }
    }
}
