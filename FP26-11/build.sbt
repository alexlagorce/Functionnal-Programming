libraryDependencies ++= Seq(
  // MongoDB Spark Connector
  "org.mongodb.spark" %% "mongo-spark-connector" % "10.1.0",

  // Apache Spark Core and SQL
  "org.neo4j" %% "neo4j-connector-apache-spark" % "5.3.1_for_spark_3",
  "org.apache.spark" %% "spark-core" % "3.5.0",
  "org.apache.spark" %% "spark-sql" % "3.5.0"
)
