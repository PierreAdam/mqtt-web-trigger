import com.typesafe.sbt.packager.MappingsHelper._
import sbt.Keys.watchSources

// Project information
name := """mqtt-web-trigger"""
description := "MQTT Web Trigger"
startYear := Some(2018)
organization := "com.jackson42"
organizationHomepage := Some(url("http://www.jackson42.com"))

// Project base
lazy val root = (project in file("."))
    .enablePlugins(PlayJava, BuildInfoPlugin)
    .settings(
        watchSources ++= (baseDirectory.value / "public" ** "*").get,
        sources in(Compile, doc) := Seq.empty,
        buildInfoKeys := BuildInfoKey.ofN(name, version, javaVersion, scalaVersion, sbtVersion),
        buildInfoPackage := "sbtbuildinfo"
    )

// Version of java
val javaVersion = settingKey[String]("The version of Java used for building.")
javaVersion := System.getProperty("java.version")

// Version of scala
scalaVersion := "2.12.8"

buildInfoOptions += BuildInfoOption.BuildTime

// Add resolvers
resolvers += "jitpack" at "https://jitpack.io"

// Add credentials
credentials += Credentials(Path.userHome / ".sbt" / ".credentials.jitpack")


// Library dependencies for java 9 and later
val java9AndSupLibraryDependencies: Seq[sbt.ModuleID] =
    if (!javaVersion.toString.startsWith("1.8")) {
        Seq(
            "com.sun.activation" % "javax.activation" % "1.2.0",
            "com.sun.xml.bind" % "jaxb-core" % "2.3.0",
            "com.sun.xml.bind" % "jaxb-impl" % "2.3.1",
            "javax.jws" % "javax.jws-api" % "1.1",
            "javax.xml.bind" % "jaxb-api" % "2.3.0",
            "javax.xml.ws" % "jaxws-api" % "2.3.1"
        )
    } else {
        Seq.empty
    }

// Library dependencies
libraryDependencies ++= Seq(
    guice,
    ws,
    "org.postgresql" % "postgresql" % "42.2.5",
    "joda-time" % "joda-time" % "2.10.2",
    "com.fasterxml.jackson.core" % "jackson-core" % "2.9.9",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.9.3",
    "commons-codec" % "commons-codec" % "1.12",
    "com.github.play-rconf" % "play-rconf" % "release~18.12",
    "com.github.play-rconf" % "play-rconf-etcd" % "release~18.12",
    "org.eclipse.paho" % "org.eclipse.paho.client.mqttv3" % "1.2.1",
    "io.logz.logback" % "logzio-logback-appender" % "1.0.22",
    "net.logstash.logback" % "logstash-logback-encoder" % "6.1",

    // Testing libraries for dealing with CompletionStage...
    "org.assertj" % "assertj-core" % "3.6.2" % Test,
    "org.awaitility" % "awaitility" % "2.0.0" % Test
) ++ java9AndSupLibraryDependencies

dependencyOverrides ++= Seq(
    "com.google.guava" % "guava" % "23.0",
)

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

// Disable the play enhancer
//playEnhancerEnabled := false

// Scala compiler options
scalacOptions in ThisBuild ++= Seq(
    "-deprecation",
    "-unchecked"
)

// Java compiler options
javacOptions in ThisBuild ++= Seq(
    "-Xlint:cast",
    "-Xlint:deprecation",
    "-Xlint:divzero",
    "-Xlint:empty",
    "-Xlint:fallthrough",
    "-Xlint:finally",
    "-Xlint:unchecked"
)

mappings in Universal ++= directory(baseDirectory.value / "public")
