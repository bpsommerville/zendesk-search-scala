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
  "org.scalatest" %% "scalatest" % "3.0.8" % "it,test"
)

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
  )