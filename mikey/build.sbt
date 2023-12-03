scalaVersion := "2.13.12"
name := "advent"
organization := "bike.mikey"
version := "0.1"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.10.0",
  "org.scalatest" %% "scalatest" % "3.2.17" % "test",
  "org.scala-lang" % "scala-reflect" % "2.13.8"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-Xlint"
)
