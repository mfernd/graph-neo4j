import Dependencies._

ThisBuild / scalaVersion := "2.12.18"
ThisBuild / version := "0.1.0"
ThisBuild / organization := "fr.umontpellier.polytech"
ThisBuild / organizationName := "Polytech Montpellier"

lazy val root = (project in file("."))
  .settings(
    name := "scala-spark",
    libraryDependencies += munit % Test,
  )

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.5.5"
// S3 connector
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "3.4.1"
libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "3.4.1"
libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % "3.4.1"
// Neo4j connector
libraryDependencies += "org.neo4j" %% "neo4j-connector-apache-spark" % "5.3.1_for_spark_3"
// Spark streaming (with Kafka)
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.5.5"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.12" % "3.5.5"
