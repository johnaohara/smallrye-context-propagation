package io.smallrye.concurrency.impl;

import org.eclipse.microprofile.concurrent.ManagedExecutor;
import org.eclipse.microprofile.concurrent.ManagedExecutorBuilder;

import io.smallrye.concurrency.SmallRyeConcurrencyManager;

public class ManagedExecutorBuilderImpl implements ManagedExecutorBuilder {

	private SmallRyeConcurrencyManager manager;
	private int maxAsync;
	private int maxQueued;
	private String[] propagated;

	public ManagedExecutorBuilderImpl(SmallRyeConcurrencyManager manager) {
		this.manager = manager;
	}

	@Override
	public ManagedExecutor build() {
		return new ManagedExecutorImpl(maxAsync, maxQueued, new ThreadContextImpl(manager, propagated, null));
	}

	@Override
	public ManagedExecutorBuilder propagated(String... types) {
		this.propagated = types;
		return this;
	}

	@Override
	public ManagedExecutorBuilder maxAsync(int max) {
		this.maxAsync = max;
		return this;
	}

	@Override
	public ManagedExecutorBuilder maxQueued(int max) {
		this.maxQueued = max;
		return this;
	}

}