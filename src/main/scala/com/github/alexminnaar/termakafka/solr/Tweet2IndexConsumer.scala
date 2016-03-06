package com.github.alexminnaar.termakafka.solr

import java.util
import java.util.Properties
import scala.collection.JavaConversions._
import kafka.consumer.ConsumerConfig

/**
  * consume tweets from the kafka message bus and index them into solr
  */
object Tweet2IndexConsumer extends App {

  val zookeeper = args(0)
  val groupId = args(1)
  val topic = args(2)
  val solrHost = args(3)

  def createConsumerConfig(zookeeper: String, groupId: String): ConsumerConfig = {

    val props = new Properties()
    props.put("zookeeper.connect", zookeeper)
    props.put("group.id", groupId)
    props.put("zookeeper.session.timeout.ms", "500")
    props.put("zookeeper.sync.time.ms", "250")
    props.put("auto.commit.interval.ms", "1000")

    new ConsumerConfig(props)
  }

  val consumer = kafka
    .consumer
    .Consumer
    .createJavaConsumerConnector(
      createConsumerConfig(zookeeper, groupId)
    )

  //number of threads to assign to each topic
  val topicMap: java.util.Map[String, Integer] = new util.HashMap[String, Integer]()

  //assign one thread to this topic
  topicMap.put(topic, new Integer(1))

  val consumerStreamsMap = consumer.createMessageStreams(topicMap)
  val streamList = consumerStreamsMap.get(topic)

  for (stream <- streamList) {

    val consumerIter = stream.iterator()

    //index messages from topic into solr
    while (consumerIter.hasNext()) {
      val tweet = SimpleTweet(new String(consumerIter.next().message()))
      SolrIndexer.indexTweet(tweet,solrHost)
      println(s"Tweet indexed at [${System.currentTimeMillis()}]")
    }

    if (consumer != null) consumer.shutdown()
  }


}
