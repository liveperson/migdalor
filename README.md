migdalor
========

Manage schema revisions over messages in Apache Kafka 

Migdalor is an open source that helps manage revisions of schemes that are used in messages through Apache Kafka.
Messages that are passed through Kafka may be created as clear text that are know to both producer and consumer.

A better approach is to create a schema that is shared between procucer and consumer and defines the messages internal scturcture.
There are plenty of schema languages that can be used for this purpose:
Avro, Protocol-Buffers, XML-Schema, Thrift etc.

The migdalor framework let you manage the synchronization of the scheme revision between the producer and consumer to make the encoding/decoding smooth and transparent.
