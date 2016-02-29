name := "terma-kafka"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "0.8.2.0",
  "org.twitter4j" % "twitter4j-stream" % "3.0.3"
)