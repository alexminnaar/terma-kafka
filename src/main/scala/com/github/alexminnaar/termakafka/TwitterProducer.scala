package com.github.alexminnaar.termakafka

import java.util.Properties

import kafka.javaapi.producer.Producer
import kafka.producer.ProducerConfig


object TwitterProducer extends App {

  val consumerKey = args(0)
  val consumerSecret = args(1)
  val accessToken = args(2)
  val accessSecret = args(3)
  val kafkaTweetTopic = args(4)

  //configure kafka producer client
  val props = new Properties()

  props.put("metadata.broker.list", "localhost:9092")
  props.put("serializer.class", "kafka.serializer.StringEncoder")
  props.put("request.required.acks", "1")
  val config = new ProducerConfig(props)

  val producer = new Producer[String, String](config)

  //configure twitter client
  val twitterConfig = TwitterUtils.config(
    consumerKey,
    consumerSecret,
    accessToken,
    accessSecret
  )

  //setup twitter stream listener which publishes to kafka using the producer we just made
  val twitterListener = TwitterUtils.simpleStatusListener(
    producer,
    kafkaTweetTopic
  )

  //create the stream
  val stream = TwitterUtils.createStream(
    twitterConfig,
    twitterListener
  )

}