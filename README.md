migdalor
========
Manage schema revisions over messages in Apache Kafka 

What does it do?
================
Migdalor is an open source that helps manage revisions of schemes that are used in messages through Apache Kafka. Messages that are passed through Kafka may be created as clear text that is known to both producer and consumer.

A better approach is to create a schema that is shared between producer and consumer and defines the messages' internal structure. There are plenty of scheme languages that can be used for this purpose: Avro, Protocol-Buffers, XML-Schema, Thrift etc.

The migdalor framework lets you manage the synchronization of the schema revisions between the producer and consumer to make the encoding/decoding smooth and transparent.

Examples of usage
=================
**1.** Avro messages that are consumed by Storm topology using Kafka-spout.

**2.** Avro messages that are consumed using Camus and written into HDFS.


Prerequisites
=============
java 1.7

maven 3.0.4




How to install?
===============
create directory migdalor in your home directory:
```bash
$ mkdir migdalor
$ cd migdalor
```
clone the migdalor project from github:
```bash
$ git clone https://github.com/liveperson/migdalor.git
```
install the project using maven:
```bash
$ mvn install
```

Run Demo:
=========
```bash
$ java -jar examples/demo/target/demo-0.0.0.1-SNAPSHOT-shaded.jar
```
 


