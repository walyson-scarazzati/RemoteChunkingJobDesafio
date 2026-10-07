package com.springbatch.remotechunkingjob.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.transaction.PlatformTransactionManager;

import com.springbatch.remotechunkingjob.dominio.Pessoa;

@Profile("manager")
@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
public class ManagerConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private RemoteChunkingManagerStepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("transactionManagerApp")
	private PlatformTransactionManager transactionManagerApp;
	
	@Bean
	public Job remoteChunkingJob(
			@Qualifier("migrarPessoaWorkerStep") Step migrarPessoaStep,
			@Qualifier("migrarDadosBancariosStep") Step migrarDadosBancariosStep
			) {
		 return jobBuilderFactory
				 .get("remoteChunkingJob")
				 .start(migrarPessoaStep)
				 .next(migrarDadosBancariosStep)
				 .incrementer(new RunIdIncrementer())
				 .build();
	}
	
	@Bean
	public Step migrarPessoaWorkerStep(
			ItemReader<Pessoa> arquivoPessoaReader
			) {
		return stepBuilderFactory
				.get("migrarPessoaWorkerStep")
				.chunk(100)
				.reader(arquivoPessoaReader)
				.outputChannel(requests())
				.inputChannel(replies())
				.transactionManager(transactionManagerApp)
				.build();
	}

	@Bean
	public QueueChannel replies() {
		return new QueueChannel();
	}

	@Bean
	public DirectChannel requests() {
		return new DirectChannel();
	}
	
}
