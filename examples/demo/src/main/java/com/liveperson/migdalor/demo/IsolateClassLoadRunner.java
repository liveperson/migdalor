package com.liveperson.migdalor.demo;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/30/13
 * Time: 4:27 PM
 * We need this class to demonstrate different processes that has different classpath.
 * in our example we use one jvm but few classLoaders
 */

import com.liveperson.schema.SchemaRepo;
import com.liveperson.schema.SchemaZkRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class IsolateClassLoadRunner {
    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(IsolateClassLoadRunner.class);

    private final CommonResources commonResources;

    private ExecutorService executor;

    private List<Future<Thread>> taskList;



    public IsolateClassLoadRunner(CommonResources commonResources) {
        this.commonResources = commonResources;
        executor = Executors.newFixedThreadPool(commonResources.numberOfThreads);
        taskList = new LinkedList<Future<Thread>>();

    }

    /**
     *
     * @param jar - jar that contains com.liveperson.migdalor.example.ExampleProducer and method runProducer
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws MalformedURLException
     */
    public void runProducer(final String jar) throws NoSuchMethodException, ClassNotFoundException, MalformedURLException {
        runClient(jar, CommonResources.EXAMPLE_PRODUCER, CommonResources.RUN_PRODUCER);
    }

    /**
     *
     * @param jar jar that contains com.liveperson.migdalor.example.ExampleConsumer and method runConsumer
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws MalformedURLException
     */
    public void runConusmer(String jar) throws NoSuchMethodException, ClassNotFoundException, MalformedURLException {
        runClient(jar, CommonResources.EXAMPLE_CONSUMER, CommonResources.RUN_CONSUMER);
    }


    /**
     * runClient the consumer or producer according to the method and class it got.
     * using classload and in a different thread sleep till interupt
     * @param jar
     * @param classString
     * @param methodString
     * @throws MalformedURLException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    private void runClient(String jar, String classString, String methodString) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException {
        URLClassLoader child = new URLClassLoader(new URL[]{new URL("file:"+jar)}, Demo.class.getClassLoader());
        Class classToLoad = Class.forName(classString, true, child);
        final Method method = classToLoad.getDeclaredMethod(methodString, Queue.class, SchemaRepo.class);
        Callable<Thread> callable = runClientMethod(method);
        taskList.add(executor.submit(callable));
    }

    private Callable<Thread> runClientMethod(final Method method) {

        return new Callable<Thread>() {
            @Override
            public Thread call() throws Exception{
                SchemaRepo<String> schemaRepo = new SchemaZkRepo(commonResources.zkSchemaRepoProperties);
                return  (Thread)method.invoke(null, commonResources.concurrentLinkedQueue, schemaRepo);
            }
        };
    }

    public synchronized void stopAll() throws InterruptedException, ExecutionException {
        for (Future<Thread> task : taskList){
            task.get().interrupt();
        }
        LOG.info("all consumer and producers stopped");
    }

    public void shutDown(){
        executor.shutdownNow();
        LOG.info("shutting down executor service");
    }



}
