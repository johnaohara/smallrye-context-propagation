package io.smallrye.concurrency.test.classloading;

import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.microprofile.concurrent.spi.ConcurrencyManagerExtension;
import org.eclipse.microprofile.concurrent.spi.ConcurrencyProvider;
import org.junit.Assert;

import io.smallrye.concurrency.SmallRyeConcurrencyManager;
import io.smallrye.concurrency.SmallRyeConcurrencyProvider;
import io.smallrye.concurrency.impl.ThreadContextProviderPlan;

public class ATest implements BiConsumer<ClassLoader,ClassLoader> {
    @Override
	public void accept(ClassLoader thisClassLoader, ClassLoader parentClassLoader) {
    	Assert.assertEquals(thisClassLoader, ATest.class.getClassLoader());
    	ConcurrencyProvider concurrencyProvider = ConcurrencyProvider.instance();
    	System.err.println("A CP: "+concurrencyProvider);
    	System.err.println("A CM: "+concurrencyProvider.getConcurrencyManager());
    	Assert.assertEquals(parentClassLoader, concurrencyProvider.getClass().getClassLoader());
    	
    	SmallRyeConcurrencyManager concurrencyManager = (SmallRyeConcurrencyManager) concurrencyProvider.getConcurrencyManager();
    	ThreadContextProviderPlan plan = concurrencyManager.getProviderPlan();
        Assert.assertEquals(1, plan.propagatedProviders.size());
        Assert.assertEquals("A", plan.propagatedProviders.iterator().next().getThreadContextType());
        Assert.assertTrue(plan.unchangedProviders.isEmpty());
        Assert.assertTrue(plan.clearedProviders.isEmpty());
        
        List<ConcurrencyManagerExtension> propagators = SmallRyeConcurrencyProvider.getManager().getExtensions();
        Assert.assertEquals(1, propagators.size());
        Assert.assertTrue(propagators.get(0).getClass() == MultiClassloadingTest.AThreadContextPropagator.class);
    }
}