package fr.umontpellier.polytech

import org.apache.spark.sql.{SparkSession, Dataset}
import org.apache.spark.sql.functions._
import org.apache.spark.SparkFiles
import org.apache.spark.SparkContext
import org.apache.spark.sql.types.{StructType, IntegerType, StringType}

object StackOverflowTest {
  def main(args: Array[String]): Unit = {
    val programStartTime = System.nanoTime()

    val spark = SparkSession
      .builder()
      .appName("StackOverflowTest")
      .master("local[*]")
      .getOrCreate()
    SparkContext.getOrCreate().setLogLevel("ERROR")

    val schema = new StructType()
      .add("postTypeId", IntegerType, nullable = true)
      .add("id", IntegerType, nullable = true)
      .add("acceptedAnswer", StringType, nullable = true)
      .add("parentId", IntegerType, nullable = true)
      .add("score", IntegerType, nullable = true)
      .add("tag", StringType, nullable = true)

    // println()
    // println(SparkFiles.get("stackoverflow.csv"))
    // println()

    val df = spark.read
      .option("header", "false")
      // .option("inferSchema", "true")
      .schema(schema)
      .csv(f"file:///${SparkFiles.get("stackoverflow.csv")}")
      .drop("acceptedAnswer")

    println(s"\nCount of records in CSV file: ${df.count()}")
    df.printSchema()
    df.show(5)

    import spark.implicits._
    println(
      "Count acceptedAnswer null: " + df
        .filter(col("acceptedAnswer").isNull)
        .count(),
      "\nCount tag null: " + df.filter($"tag".isNull).count()
        + "\nCount parentId null: " + df.filter($"parentId".isNull).count()
    )

    // Filter posts with a score greater than 10
    val highScorePosts = df
      .filter(col("score") > 20)
    highScorePosts.show(5)

    // Register the DataFrame as a SQL temporary view
    df.createOrReplaceTempView("stackoverflow")

    // Query 1: Top 5 highest scores
    val top5Scores = spark.sql(
      "SELECT id, score FROM stackoverflow ORDER BY score DESC LIMIT 5"
    )
    println("top5Scores (without tags):")
    top5Scores.show()
    val top5ScoresWithTag = spark.sql("""
    SELECT id, score, tag
    FROM stackoverflow
    WHERE tag IS NOT NULL
    ORDER BY score DESC
    LIMIT 5
    """)
    println("top5ScoresWithTag:")
    top5ScoresWithTag.show()

    // Query 2: Most frequently used tags
    val popularTags = spark.sql("""
      SELECT tag, COUNT(1) as frequency
      FROM stackoverflow
      WHERE tag IS NOT NULL
      GROUP BY tag
      ORDER BY frequency DESC
      LIMIT 10
    """)
    println("Most frequently used tags:")
    popularTags.show()

    val programElapsedTime = (System.nanoTime() - programStartTime) / 1e9
    println(s"\nProgram execution time: $programElapsedTime seconds")

    spark.stop()
  }
}
