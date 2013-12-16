import sbt._
import Keys._

object ProjectBuild extends Build {
  lazy val root = Project(
    id ="xlsaccessor",  // Set your project name here (artifact-id)
    base = file("."),
    settings =
      Defaults.defaultSettings
      //  ++ Seq(PackageTask.packageDistTask)
      //  ++ PackageTask.distSettings
        ++ Seq(
        scalaVersion := "2.9.2",
        organization := "com.jellyfish85", // groupidを設定
        version := "1.0-SNAPSHOT",
        scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked"),
        parallelExecution := true,
        crossPaths := false
      )
  )
}