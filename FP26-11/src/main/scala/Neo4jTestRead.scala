import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}

object Neo4jTestRead {
  def main(args: Array[String]): Unit = {
    val programStartTime = System.nanoTime()

    // Set logging level to avoid excessive logs
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Replace with the actual connection URI and credentials
    val url = "neo4j+s://c924c8b8.databases.neo4j.io"
    val password = "slBPuTRz_8RtypnuxYQEa-DDbRqoPAxTvRyggM8BBF0"
    val username = "neo4j"
    val dbname = "neo4j"

    // Initialize Spark session with Neo4j configuration
    val spark = SparkSession.builder
      .config("neo4j.url", url)
      .config("neo4j.authentication.basic.username", username)
      .config("neo4j.authentication.basic.password", password)
      .config("neo4j.database", dbname)
      .appName("Spark App")
      .master("local[*]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    // Define the query to read data from Neo4j
    val readQuery =
      """
        |MATCH (n)
        |RETURN COUNT(n)
      """.stripMargin

    // Execute the query and load the results into a DataFrame
    val df = spark.read
      .format("org.neo4j.spark.DataSource")
      .option("query", readQuery)
      .load()

    // Show the results
    df.show()

    // Calculate and display program execution time
    val programElapsedTime = (System.nanoTime() - programStartTime) / 1e9
    println(s"\nProgram execution time: $programElapsedTime seconds")
    println("\n.......Program *****Completed***** Successfully.....!\n")

    // Stop the Spark session
    spark.stop()
  }
}
