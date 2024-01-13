package io.demo;

import jakarta.persistence.EntityManagerFactory;
import model.Data;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


@Configuration
@EnableBatchProcessing

public class BatchConfig {

        @Autowired
        private JobBuilderFactory jobBuilderFactory;

        @Autowired
        private StepBuilderFactory stepBuilderFactory;

        @Autowired
        private ItemReader<DataInput1> itemReader;

        @Autowired
        private ItemProcessor<DataInput1, Data> itemProcessor;

        @Autowired
        private ItemWriter<Data> itemWriter;

        @Autowired
        private EntityManagerFactory entityManagerFactory;


        @Bean
        public Job myJob() {
            return jobBuilderFactory.get("myJob")
                    .incrementer(new RunIdIncrementer())
                    .start(myStep())
                    .build();
        }

        @Bean
        public Step myStep() {
            return stepBuilderFactory.get("myStep")
                    .<DataInput1, Data>chunk(10)
                    .reader(itemReader)
                    .processor(itemProcessor)
                    .writer(itemWriter)
                    .build();
        }

        @Bean
        public ItemReader<DataInput1> itemReader() {

            FlatFileItemReader<DataInput1> reader = new FlatFileItemReader<>();
            reader.setResource(new ClassPathResource("Data.csv"));  // Specify your CSV file location
            reader.setLinesToSkip(1);  // Skip the header line
            reader.setLineMapper(new DefaultLineMapper<DataInput1>() {{
                setLineTokenizer(new DelimitedLineTokenizer() {{
                    setNames("endYear", "intensity", "city", "country", "relevance", "likelihood", "topic", "region", "startYear");
                }});
                setFieldSetMapper(new BeanWrapperFieldSetMapper<DataInput1>() {{
                    setTargetType(DataInput1.class);
                }});
            }});
            return reader;
        }

        @Bean
        public ItemProcessor<DataInput1, Data> itemProcessor() {
            return new DataProcessor();
        }

        @Bean
        public ItemWriter<Data> itemWriter() {
            JpaItemWriter<Data> writer = new JpaItemWriter<>();
            writer.setEntityManagerFactory(entityManagerFactory);
            return writer;
        }
    }


