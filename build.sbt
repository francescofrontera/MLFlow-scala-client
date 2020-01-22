name := "mlflow-scala-client"

version := "0.1"

scalaVersion in ThisBuild := "2.13.1"

lazy val dispatchhttpVersion = "1.2.0"
lazy val zioVersion          = "1.0.0-RC17"
lazy val pureConfigVersion   = "0.12.2"
lazy val circeVersion        = "0.12.3"

lazy val core = (project in file("core"))
  .settings(
    libraryDependencies ++= Seq(
      "org.dispatchhttp"      %% "dispatch-core" % dispatchhttpVersion,
      "com.github.pureconfig" %% "pureconfig"    % pureConfigVersion,
      "dev.zio"               %% "zio-streams"   % zioVersion
    ) ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )

lazy val examples = (project in file("examples")).dependsOn(core)
