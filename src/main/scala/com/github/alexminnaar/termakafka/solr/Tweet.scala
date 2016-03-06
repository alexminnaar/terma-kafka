package com.github.alexminnaar.termakafka.solr

sealed trait Tweet

case class SimpleTweet(text:String) extends Tweet
