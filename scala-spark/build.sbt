import Dependencies._

ThisBuild / scalaVersion     := "2.12.18"
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "fr.umontpellier.polytech"
ThisBuild / organizationName := "Polytech Montpellier"

lazy val root = (project in file("."))
  .settings(
    name := "scala-spark",
    libraryDependencies += munit % Test
  )

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.5.5"
// Neo4j connector
libraryDependencies += "org.neo4j" %% "neo4j-connector-apache-spark" % "5.3.1_for_spark_3"
// Spark streaming (with Kafka)
libraryDependencies += "org.apache.spark" % "spark-streaming_2.12" % "3.5.5"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.12" % "3.5.5"