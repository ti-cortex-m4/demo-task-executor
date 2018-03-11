import de.johoop.testngplugin.TestNGPlugin._

testNGSettings

name := "demo-task-executor-scala"

version := "0.0.1-SNAPSHOT"

organization := "demo"

scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.sbtPluginRepo("releases")

libraryDependencies ++= {
  Seq(
    "org.testng" % "testng" % "6.9.10"
  )
}

testNGVersion := "6.9.10"

testNGOutputDirectory := "target/tesng"

testNGSuites := Seq("src/test/resources/testng.xml")

