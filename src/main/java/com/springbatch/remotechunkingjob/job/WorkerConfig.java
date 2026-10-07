package com.springbatch.remotechunkingjob.job;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;

import com.springbatch.remotechunkingjob.dominio.Pessoa;

@Profile("worker")
@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
public class WorkerConfig {
	@Autowired
	private RemoteChunkingWorkerBuilder<Pessoa, Pessoa> pessoaWorkerBuilder;
	
	@Bean
	public IntegrationFlow integrationFlow(
			ItemProcessor<Pessoa, Pessoa> pessoaItemProcessor,
			ItemWriter<Pessoa> pessoaItemWriter) {
		return pessoaWorkerBuilder
				.itemProcessor(pessoaItemProcessor)
				.itemWriter(pessoaItemWriter)
				.inputChannel(requests())
				.outputChannel(replies())
				.build();		
	}
	
	@Bean
	public DirectChannel requests() {
		return new DirectChannel();
	}
	
	@Bean
	public DirectChannel replies() {
		return new DirectChannel();
	}
	
}
