name := "zendesk-search"
description := """Zendesk coding challenge"""
organization := "au.id.sommerville"
organizationName := "Ben Sommerville"
homepage := Some(url("https://github.com/bpsommerville/zendesk-search-scala"))
version := "1.0.0"

scalaVersion := "2.13.0"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.7.1",
  "com.lihaoyi" %% "upickle" % "0.7.5",
  "org.scalatest" %% "scalatest" % "3.0.8" % "it,test",
  "org.scalamock" %% "scalamock" % "4.3.0" % "it,test",
  "com.github.pjfanning" %% "scala-faker" % "0.5.0" % "it,test"
)

fork in IntegrationTest := true
javaOptions ++= Seq("-Xms2048M", "-Xmx8196M", "-XX:+CMSClassUnloadingEnabled")
dependencyClasspath in IntegrationTest := (dependencyClasspath in IntegrationTest).value ++ (exportedProducts in Test).value

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
  )