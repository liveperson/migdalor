package com.liveperson.migdalor.demo;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/25/13
 * Time: 12:13 PM
 */

import com.liveperson.schema.RepoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo {
    /**
     * slf4j Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(Demo.class);
    public static final String HASH_KEY_SEPARATOR = "############################################################";
    public static final String MINUS_KEY_SEPARATOR = "----------------------------------------------------------------------------------------------------------------------------------------";

    private final String DEMO_SUB = "DEMO SUBTITLES:";


    CommonResources commonResources;

    public Demo() throws RepoException {
        commonResources = new CommonResources();

    }

    /**
     * DEMO:
     * 1. run local zk
     * 2. run 2 producers with new and old avro schema
     * 3. run consumer that will read those events with the old schema. stop producers and consumer
     * 4. same as 2 and 3 but with consumer that has new avro schema
     * 5. stop local zk
     *
     * @throws InterruptedException
     */
    private void runDemo() throws InterruptedException {
        commonResources.startZk();
        String [] jars = CommonResources.getProducerConsumerJars();
        IsolateClassLoadRunner classLoadRunner = new IsolateClassLoadRunner(commonResources);
        try {


            LOG.info("{} we have [{}] schema versions, we will demonstrate two scenarios:\n",DEMO_SUB,jars.length);
            /**
             * Scenario 1
             */

            LOG.info("{} " + HASH_KEY_SEPARATOR + "   SCENARIO 1   " + HASH_KEY_SEPARATOR,DEMO_SUB);
            LOG.info("{} In this scenario we run two producers with old and new schema versions",DEMO_SUB);
            LOG.info("{} respectively and one consumer of OLD version",DEMO_SUB);
            LOG.info("{} " + MINUS_KEY_SEPARATOR,DEMO_SUB);
            LOG.info("{} running producer with schema version 1 - simple event with color",DEMO_SUB);
            classLoadRunner.runProducer(jars[0]);
            LOG.info("{} running producer with schema version 2 - updated event uses a new type field and got rid of the color field",DEMO_SUB);
            classLoadRunner.runProducer(jars[1]);
            LOG.info("{} running consumer with schema version 1 to consumer all events from both versions", DEMO_SUB);
            classLoadRunner.runConusmer(jars[0]);
            Thread.sleep(1000 * 20);
            classLoadRunner.stopAll();
            Thread.sleep(1000 * 2);
            LOG.info("{} " + HASH_KEY_SEPARATOR + " END SCENARIO 1 " + HASH_KEY_SEPARATOR+"\n\n",DEMO_SUB);

            /**
             * Scenario 2
             */

            LOG.info("{} " + HASH_KEY_SEPARATOR + "   SCENARIO 2   " + HASH_KEY_SEPARATOR,DEMO_SUB);
            LOG.info("{} In this scenario we run two producers with old and new schema versions",DEMO_SUB);
            LOG.info("{} respectively and one consumer of NEW version",DEMO_SUB);
            LOG.info("{} " + MINUS_KEY_SEPARATOR,DEMO_SUB);
            LOG.info("{} running producer with schema version 1 - simple event with color",DEMO_SUB);
            classLoadRunner.runProducer(jars[0]);
            LOG.info("{} running producer with schema version 2 - updated event uses a new type field and got rid of the color field",DEMO_SUB);
            classLoadRunner.runProducer(jars[1]);
            LOG.info("{} running consumer with schema version 2 to consumer all events from both versions", DEMO_SUB);
            classLoadRunner.runConusmer(jars[1]);
            Thread.sleep(1000 * 20);
            classLoadRunner.stopAll();
            Thread.sleep(1000 * 2);
            LOG.info("{} " + HASH_KEY_SEPARATOR + " END SCENARIO 2 " + HASH_KEY_SEPARATOR+"\n\n", DEMO_SUB);
            classLoadRunner.shutDown();

        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        Thread.sleep(1000 * 20);

        commonResources.stopZK();
    }



    public static void main(String[] args) throws InterruptedException, RepoException {
        Demo demo = new Demo();
        demo.runDemo();



    }


}
