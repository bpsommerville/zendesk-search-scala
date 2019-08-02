name := "zendesk-search"
description := """Zendesk coding challenge"""
organization := "au.id.sommerville"
organizationName := "Ben Sommerville"
homepage := Some(url("https://github.com/bpsommerville/zendesk-search-scala"))
licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

scalaVersion := "2.13.0"

libraryDependencies += "com.github.scopt" %% "scopt" % "3.7.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

