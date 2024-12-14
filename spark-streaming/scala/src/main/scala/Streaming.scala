import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory

object Streaming {
  def main(args: Array[String]): Unit = {

    val logger = LoggerFactory.getLogger(getClass.getName)

    logger.info("Starting Spark application: Spark Streaming With Scala and Kafka")

    val spark = SparkSession
      .builder()
      .appName("Spark Streaming With Scala and Kafka")
      .master("spark://spark:7077")
      .getOrCreate()

    logger.info("SparkSession created successfully")

    import spark.implicits._

    spark.sparkContext.setLogLevel("ERROR")
    logger.info("Log level set to ERROR")


    try {
      val df = spark.readStream
        .format("kafka")
        .option("kafka.bootstrap.servers", "kafka:9092")
        .option("subscribe", "test-topic")
        .load()

      logger.info("Kafka readStream initialized successfully")

      val rawDF = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)").as[(String, String)]
      logger.info("Raw data frame created with schema: " + rawDF.schema.treeString)

      val query = rawDF.writeStream
        .outputMode("update")
        .format("console")
        .start()

      logger.info("Streaming query started")

      query.awaitTermination()
      logger.info("Streaming query terminated")
    } catch {
      case e: Exception =>
        logger.error("Error occurred in streaming application: ", e)
    }
  }
}
