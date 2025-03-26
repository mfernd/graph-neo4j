package fr.umontpellier.polytech

import org.apache.spark.sql.{SparkSession, Dataset}
import org.apache.spark.sql.functions._
import org.apache.spark.SparkFiles
import org.apache.spark.SparkContext

object UsersTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("UsersTest")
      .master("local[*]")
      .getOrCreate()

    // println("Listing files in the SparkContext:")
    // SparkContext.getOrCreate().listFiles().foreach(println)
    // println()

    import spark.implicits._
    val usersDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(f"file:///${SparkFiles.get("users.csv")}")

    val result = usersDF
      .filter($"age" >= 25)
      .groupBy("city")
      .agg(collect_list("name").as("names"))
      .select("city", "names")

    val resultCollected = result.collect()
    resultCollected.foreach { row =>
      val city = row.getAs[String]("city")
      val names = row.getAs[Seq[String]]("names")
      println(s"Users in $city: ${names.mkString(", ")}")
    }
    spark.stop()
  }
}
