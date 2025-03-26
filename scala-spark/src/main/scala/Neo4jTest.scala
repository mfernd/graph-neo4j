package fr.umontpellier.polytech

import org.apache.spark.sql.SparkSession

object Neo4jTest {
  def main(args: Array[String]): Unit = {
    val url = "bolt://neo4j-standalone.neo4j.svc.cluster.local:7687"
    val password = "CHANGEME"
    val username = "neo4j"
    val dbname = "neo4j"

    val spark = SparkSession.builder
      .config("neo4j.url", url)
      .config("neo4j.authentication.basic.username", username)
      .config("neo4j.authentication.basic.password", password)
      .config("neo4j.database", dbname)
      .appName("Neo4jTest")
      .master("local[*]")
      .getOrCreate()

    val readQuery =
      """
      MATCH (n)
      RETURN COUNT(n)
      """

    val df = spark.read
      .format("org.neo4j.spark.DataSource")
      .option("query", readQuery)
      .load()

    df.show()

    spark.stop()
  }
}
