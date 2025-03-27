package fr.umontpellier.polytech

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{StreamingContext, Seconds}
import org.apache.spark.sql.types._

object MinioStreamingTest extends App {
  val minio_endpoint = "http://minio.minio.svc.cluster.local:9000"
  val accessKeyId = "minioadmin"
  val accessKey = "minioadmin"

  val spark = SparkSession
    .builder()
    .appName("MinIO Streaming Test")
    .config(
      "spark.hadoop.fs.s3a.impl",
      "org.apache.hadoop.fs.s3a.S3AFileSystem"
    )
    .config("spark.hadoop.fs.s3a.path.style.access", "true")
    .config("spark.hadoop.fs.s3a.endpoint", minio_endpoint)
    .config("spark.hadoop.fs.s3a.access.key", accessKeyId)
    .config("spark.hadoop.fs.s3a.secret.key", accessKey)
    .config("spark.hadoop.fs.s3a.connection.ssl.enabled", "false")
    .config("spark.sql.streaming.schemaInference", "true")
    .master("local[*]")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR")

  val schema = new StructType()
    .add("id", IntegerType)
    .add("name", StringType)
    .add("age", IntegerType)
    .add("city", StringType)

  val streamingDF = spark.readStream
    .option("header", "true")
    .schema(schema)
    .csv("s3a://mybucket/input/")

  val consoleQuery = streamingDF.writeStream
    .format("console")
    .outputMode("append") // each new file’s rows will appear in the console
    .start()

  val csvQuery = streamingDF.writeStream
    .format("csv")
    .outputMode("append")
    .option("path", "s3a://mybucket/output/")
    .option(
      "checkpointLocation",
      "chekpoint/"
    )
    .start()

  spark.streams.awaitAnyTermination()
}
