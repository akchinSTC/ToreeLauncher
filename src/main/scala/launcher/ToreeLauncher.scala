
package launcher

import java.nio.file.{Paths, Files}
import java.util.UUID.randomUUID
//import org.apache.spark.SparkConf
//import org.apache.toree.Main
//import org.apache.toree.utils.FileUtils

object ToreeLauncher extends App{

  def isFileExist(filePath : String) : Boolean = Files.exists(Paths.get(filePath))

  var profilePath : String = null
  var kernelID : String = null
  var outputDir : String = "/tmp/" + randomUUID.toString.substring(4)
  var kernelName : String = "apache-toree-scala"

  if (args != null) {
    args.foreach(println)
    args.sliding(2, 1).toList.collect {
      case Array("--profile", arg: String) => profilePath = arg
      case Array("--name", arg: String) => kernelID = arg
      case Array("--runtimedir", arg: String) => outputDir = arg
      case Array("--kernel_name", arg: String) => kernelName = arg
    }
  }

  if (profilePath == null || profilePath.trim.length == 0 || !isFileExist(profilePath)){
    println("The profile path is invalid, creating one...")

    FileUtils.createManagedTempDirectory(outputDir)

    val kProfile = new KernelProfile(kernelID, kernelName)
    val outputPath = kProfile.createProfile(outputDir)

    if (isFileExist(outputPath)){
      println("%s saved".format(outputPath))
    }
  }

  //Main.main(args)
  println("Done")
}
