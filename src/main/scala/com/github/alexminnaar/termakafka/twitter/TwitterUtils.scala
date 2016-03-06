package com.github.alexminnaar.termakafka.twitter

import kafka.javaapi.producer.Producer
import kafka.producer.KeyedMessage
import twitter4j._
import twitter4j.conf.ConfigurationBuilder


object TwitterUtils {

  //configure the twitter api (using twitter4j)
  def config(consumerKey: String,
             consumerSecret: String,
             accessToken: String,
             accessTokenSecret: String): ConfigurationBuilder = {

    new ConfigurationBuilder()
      .setOAuthConsumerKey(consumerKey)
      .setOAuthConsumerSecret(consumerSecret)
      .setOAuthAccessToken(accessToken)
      .setOAuthAccessTokenSecret(accessTokenSecret)
  }

  /**
    * Create a twitter stream with the given twitter api config and listener (which publishes to kafka)
    *
    * @param config   Twitter api config created using api secret keys
    * @param listener Listener which publishes to kafka.
    */
  def createStream(config: ConfigurationBuilder,
                   listener: StatusListener): Unit = {

    val twitterStream = new TwitterStreamFactory(config.build()).getInstance

    twitterStream.addListener(listener)

    twitterStream.sample()
  }

  /**
    * For a given tweet, publish it with the given producer and topic
    *
    * @param kafkaProducer kafka producer
    * @param kafkaTopic    kafka topic
    * @param tweet         tweet to publish
    */
  def publishTweet(kafkaProducer: Producer[String, String],
                   kafkaTopic: String,
                   tweet: String): Unit = {

    //create publishable message
    val tweetMessage = new KeyedMessage[String, String](kafkaTopic, tweet)

    kafkaProducer.send(tweetMessage)
  }


  /**
    * Twitter api listener i.e. a place to define what to do when a new tweet comes in.
    * @return twitter listener
    */
  def simpleStatusListener(kafkaProducer: Producer[String, String],
                           kafkaTopic: String): StatusListener = {

    new StatusListener() {

      override def onStallWarning(warning: StallWarning): Unit = {}

      override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}

      override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {}

      //when a tweet comes in get the text and publish to kafka
      override def onStatus(status: Status): Unit = {

        val tweetText = status.getText

        publishTweet(
          kafkaProducer,
          kafkaTopic,
          tweetText
        )
      }

      override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {}

      override def onException(ex: Exception): Unit = {}
    }

  }

}
