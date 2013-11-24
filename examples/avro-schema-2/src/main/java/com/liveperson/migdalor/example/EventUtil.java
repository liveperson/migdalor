package com.liveperson.migdalor.example;/**
 * Created with IntelliJ IDEA.
 * User: ehudl
 * Date: 9/24/13
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */

import com.liveperson.example.Event;
import org.apache.avro.Schema;

public interface EventUtil {

    String VERSION = "version";

    Schema schema = Event.SCHEMA$;

    String version = schema.getField(VERSION).defaultValue().getTextValue().toString();

}
