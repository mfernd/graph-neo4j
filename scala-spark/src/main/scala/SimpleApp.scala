package fr.umontpellier.polytech

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkFiles
import org.apache.spark.SparkContext

object SimpleApp {
  def main(args: Array[String]): Unit = {
    val spark =
      SparkSession
        .builder()
        .appName("Simple Application")
        .master("local[*]")
        .getOrCreate()

    // println("Listing files in the SparkContext:")
    // SparkContext.getOrCreate().listFiles().foreach(println)
    // println()

    val logData = spark.read.textFile(f"file:///${SparkFiles.get("README.md")}")

    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")

    spark.stop()
  }
}
