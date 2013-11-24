migdalor
========
Manage schema revisions over messages in Apache Kafka 

What does it do?
================
Migdalor is an open source that helps manage revisions of schemes that are used in messages through Apache Kafka.
Messages that are passed through Kafka may be created as clear text that are known to both producer and consumer.

A better approach is to create a schema that is shared between procucer and consumer and defines the messages internal sctucture.
There are plenty of schema languages that can be used for this purpose:
Avro, Protocol-Buffers, XML-Schema, Thrift etc.

The migdalor framework let you manage the synchronization of the scheme revision between the producer and consumer to make the encoding/decoding smooth and transparent.

How to install?
===============
**1)**  $ mkdir migdalor

**2)**  $ cd migdalor

**3)**  $ git clone https://github.com/liveperson/migdalor.git

**4)**  $ mvn build
 


