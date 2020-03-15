name := "mlflow-scala-client"

version := "0.1"

scalaVersion in ThisBuild := "2.13.1"

lazy val sttpVersion       = "2.0.0-RC6"
lazy val pureConfigVersion = "0.12.2"
lazy val zioVersion        = "1.0.0-RC17"
lazy val circeVersion      = "0.13.0"

lazy val core = (project in file("core"))
  .settings(
    scalacOptions ++= Seq(
      "-deprecation", // Emit warning and location for usages of deprecated APIs.
      "-encoding",
      "utf-8", // Specify character encoding used by source files.
      "-explaintypes", // Explain type errors in more detail.
      "-feature", // Emit warning and location for usages of features that should be imported explicitly.
      "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
      "-language:higherKinds", // Allow higher-kinded types
      "-language:postfixOps", // Allow postfix operator notation, such as `1 to 10 toList'
      "-language:implicitConversions", // Allow definition of implicit functions called views
      "-unchecked", // Enable additional warnings where generated code depends on assumptions.
      "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
      "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
      "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
      "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
      "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
      "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
      "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
      "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
      "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
      "-Xlint:option-implicit", // Option.apply used implicit view.
      "-Xlint:package-object-classes", // Class or object defined in package object.
      "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
      "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
      "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
      "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
      "-Ywarn-dead-code", // Warn when dead code is identified.
      "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
      "-Ywarn-numeric-widen", // Warn when numerics are widened.
      "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
      "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
      "-Ywarn-unused:locals", // Warn if a local definition is unused.
      "-Ywarn-unused:params", // Warn if a value parameter is unused.
      "-Ywarn-unused:privates", // Warn if a private member is unused.
      "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
      "-Ywarn-macros:before", // via som
      "-Yrangepos" // for longer squiggles
    ),
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.client" %% "core"                          % sttpVersion,
      "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % sttpVersion,
      "com.softwaremill.sttp.client" %% "circe"                         % sttpVersion,
      "io.circe"                     %% "circe-generic"                 % circeVersion,
      //Test
      "dev.zio" %% "zio-test"     % zioVersion % "test",
      "dev.zio" %% "zio-test-sbt" % zioVersion % "test",
    ),
    testFrameworks ++= Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )

lazy val examples = (project in file("examples"))
  .dependsOn(core)
  .settings(
    fork in run := true
  )
