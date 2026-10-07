package com.springbatch.remotechunkingjob;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RemoteChunkingJobApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(RemoteChunkingJobApplication.class, args);
		if (Arrays.toString(args).contains("manager")) context.close();
	}

}
