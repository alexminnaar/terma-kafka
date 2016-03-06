name := "terma-kafka"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "0.8.2.0",
  "org.twitter4j" % "twitter4j-stream" % "3.0.3",
  "org.apache.solr" % "solr-solrj" % "5.2.1",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.7" % "runtime",
  "org.slf4j" % "slf4j-simple" % "1.7.7"
)