package com.axual.demo.config;

import com.axual.demo.entity.Pokemon;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public FlatFileItemReader<Pokemon> reader() {
        BeanWrapperFieldSetMapper<Pokemon> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(Pokemon.class);

        return new FlatFileItemReaderBuilder<Pokemon>()
                .name("pokemonItemReader")
                .resource(new ClassPathResource("Pokemon.csv"))
                .delimited()
                .names("code","name","type_1","type_2","total","hp","attack","defense","s_attack","s_defense","speed","generation","legendary")
                .lineMapper(lineMapper())
                .fieldSetMapper(mapper)
                .build();
    }

    @Bean
    public LineMapper<Pokemon> lineMapper() {

        final DefaultLineMapper<Pokemon> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("code","name","type_1","type_2","total","hp","attack","defense","s_attack","s_defense","speed","generation","legendary");

        final PokemonFieldSetMapper fieldSetMapper = new PokemonFieldSetMapper();
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

    @Bean
    public PokemonProcessor processor() {
        return new PokemonProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Pokemon> writer(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Pokemon>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO pokemon (code , name , type_1 , type_2 , total , hp , attack , defense , s_attack , s_defense , speed , generation , legendary) VALUES (:code , :name , :typeOne , :typeTwo , :total , :hp , :attack , :defense , :spAttack , :spDefense , :speed , :generation , :legendary)")
                .dataSource(dataSource)
                .build();
    }

    @Bean(name = "PokemonJob")
    public Job importPokemonJob(NotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importPokemonJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Pokemon> writer) {
        return stepBuilderFactory.get("step1")
                .<Pokemon, Pokemon> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}
