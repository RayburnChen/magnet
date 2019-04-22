package org.nfa.base.service.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadPoolConfigurer {

	// see org.springframework.boot.task.TaskSchedulingAutoConfiguration

	@Bean
	public TaskSchedulerBuilder taskSchedulerBuilder(TaskSchedulingProperties properties,
			ObjectProvider<TaskSchedulerCustomizer> taskSchedulerCustomizers) {
		TaskSchedulerBuilder builder = new TaskSchedulerBuilder();
		builder = builder.poolSize(properties.getPool().getSize());
		builder = builder.threadNamePrefix(properties.getThreadNamePrefix());
		builder = builder.customizers(taskSchedulerCustomizers);
		return builder;
	}

}
