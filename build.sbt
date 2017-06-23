name := "ToreeLauncher"

version := "1"

scalaVersion := "2.11.8"

val parentPath = "file:///Users/kunliu/git/ToreeLauncher"
val jarPath = "%s/jars".format(parentPath)

libraryDependencies += "net.liftweb" % "lift-json_2.11" % "2.6.3" from "%s/%s.jar".format(jarPath, "lift-json")

libraryDependencies += "com.thoughtworks.paranamer" % "paranamer" % "2.8" from "%s/%s.jar".format(jarPath, "paranamer")

// for proof of concept, import org.apache should work
// libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.0"

// use %% instead of %
libraryDependencies += "org.apache.toree" %% "toree" % "2.11" from "%s/%s.jar".format(jarPath, "toree")
