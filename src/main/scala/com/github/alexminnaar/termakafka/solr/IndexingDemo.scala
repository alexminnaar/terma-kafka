package com.github.alexminnaar.termakafka.solr

/**
  * Ignore this - just checking that the Solr indexer actually works
  */
object IndexingDemo extends App{

  val hostname = "http://localhost:8983/solr/gettingstarted/"

  val testTweet1= SimpleTweet("Hey I'm tweeting for the first time")

  val testTweet2 = SimpleTweet("Hey I'm tweeting for the second time")

  SolrIndexer.indexTweet(testTweet1,hostname)

  SolrIndexer.indexTweet(testTweet2,hostname)

}
