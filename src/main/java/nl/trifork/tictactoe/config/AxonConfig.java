package nl.trifork.tictactoe.config;

import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

//	@Bean
	public EventStorageEngine storageEngine() {
		return new InMemoryEventStorageEngine();
	}

}
