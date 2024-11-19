import org.apache.spark.sql.functions._

// Aplatir les éléments de CVE_Items
val explodedDF = cveDF.withColumn("CVE_Item", explode($"CVE_Items"))

// Extraire les champs nécessaires dans un nouveau DataFrame
val cveFlatDF = explodedDF.select(
  $"CVE_Item.cve.CVE_data_meta.ID".as("ID"),
  $"CVE_Item.cve.CVE_data_meta.ASSIGNER".as("Assigner"),
  $"CVE_Item.impact.baseMetricV3.cvssV3.baseScore".as("BaseScore"),
  $"CVE_Item.impact.baseMetricV3.cvssV3.baseSeverity".as("BaseSeverity"),
  $"CVE_Item.publishedDate".as("PublishedDate")
)

// Enregistrer cette vue temporaire pour utiliser Spark SQL
cveFlatDF.createOrReplaceTempView("cve_table")

val top5Scores = spark.sql("""
  SELECT ID, Assigner, BaseScore
  FROM cve_table
  ORDER BY BaseScore DESC
  LIMIT 5
""")
top5Scores.show()

val highRiskCVEs = spark.sql("""
  SELECT ID, Assigner, BaseScore, BaseSeverity
  FROM cve_table
  WHERE BaseScore > 7
  ORDER BY BaseScore DESC
""")
highRiskCVEs.show()

val recentCVEs = spark.sql("""
  SELECT ID, Assigner, BaseScore, PublishedDate
  FROM cve_table
  WHERE PublishedDate > '2024-01-01'
  ORDER BY PublishedDate DESC
""")
recentCVEs.show()
