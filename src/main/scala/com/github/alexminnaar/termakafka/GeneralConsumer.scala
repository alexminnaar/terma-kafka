package com.github.alexminnaar.termakafka

import java.util
import java.util.Properties
import kafka.consumer.ConsumerConfig
import scala.collection.JavaConversions._

/**
  * Consume from any kafka topic.  Used for debugging purposes.
  */
object GeneralConsumer extends App {

  val zookeeper = args(0)
  val groupId = args(1)
  val topic = args(2)

  def createConsumerConfig(zookeeper: String, groupId: String): ConsumerConfig = {

    val props = new Properties()
    props.put("zookeeper.connect", zookeeper)
    props.put("group.id", groupId)
    props.put("zookeeper.session.timeout.ms", "500")
    props.put("zookeeper.sync.time.ms", "250")
    props.put("auto.commit.interval.ms", "1000")

    new ConsumerConfig(props)
  }

  val consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig(zookeeper, groupId))
  val topicMap: java.util.Map[String, Integer] = new util.HashMap[String, Integer]()

  topicMap.put(topic, new Integer(1))

  val consumerStreamsMap = consumer.createMessageStreams(topicMap)

  val streamList = consumerStreamsMap.get(topic)

  for (stream <- streamList) {

    val consumerIter = stream.iterator()

    while (consumerIter.hasNext()) {
      println("Message from single topic ::" + new String(consumerIter.next().message()))
    }

    if (consumer != null) consumer.shutdown()
  }

}
