package com.github.alexminnaar.termakafka.solr

import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.apache.solr.common.{SolrInputDocument, SolrDocument}


object SolrIndexer {

  /**
    * Index a tweet into Solr
    * @param tweet a simple tweet just containing text
    * @param solrHost host and port of solr
    */
  def indexTweet(tweet: SimpleTweet,
                 solrHost: String): Unit = {

    val server = new HttpSolrServer(solrHost)

    val doc = new SolrInputDocument()

    doc.addField("text", tweet.text)

    server.add(doc)

    server.commit()

    println("tweet indexed")
    server.close()
  }


}
