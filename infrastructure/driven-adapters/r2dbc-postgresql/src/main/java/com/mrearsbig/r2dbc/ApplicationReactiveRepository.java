package com.mrearsbig.r2dbc;

import java.util.UUID;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mrearsbig.r2dbc.data.ApplicationData;

// TODO: This file is just an example, you should delete or modify it
public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationData, UUID>, ReactiveQueryByExampleExecutor<ApplicationData> {

}
