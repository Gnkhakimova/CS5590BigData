// scalastyle:off println
//package org.apache.spark.examples.mllib
import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.util.MLUtils
// $example off$

object NaiveBayesExample {

  def main(args: Array[String]): Unit = {

    //System.setProperty("hadoop.home.dir", "C:\\winutils")
    //val conf = new SparkConf().setAppName("NaiveBayesExample")
   // val sc = new SparkContext(conf)
    val conf = new SparkConf().setAppName("Samples").setMaster("local")
    val sc = new SparkContext(conf)

    // $example on$
    // Load and parse the data file.
    val data = MLUtils.loadLibSVMFile(sc, "/home/gulnoza/Downloads/ScalaMachineLearning/src/main/scala/sample_libsvm_data.txt")

    // Split data into training (60%) and test (40%).
    val Array(training, test) = data.randomSplit(Array(0.7, 0.3))

    val model = NaiveBayes.train(training, lambda = 2.0, modelType = "multinomial")

    val predictionAndLabel = test.map(p => (model.predict(p.features), p.label))
    val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()

    println(s"Prediction = $accuracy")
    // Save and load model
    model.save(sc, "target/tmp/myNaiveBayesModel")
    val sameModel = NaiveBayesModel.load(sc, "target/tmp/myNaiveBayesModel")
    // $example off$

    sc.stop()
  }
}

// scalastyle:on println