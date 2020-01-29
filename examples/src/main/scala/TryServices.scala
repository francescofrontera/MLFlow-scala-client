import io.github.francescofrontera.client.MLFlowClient
import io.github.francescofrontera.client.Executor._

object TryServices {

  def main(args: Array[String]): Unit = {
    val clientResult = MLFlowClient("http://localhost:5000/api/2.0/preview/mlflow") { modules =>
      val (experiments, run, console) = (
        modules.experimentService,
        modules.runService,
        modules.console
      )

      for {
        allExperiments <- experiments.getAll

        _ <- console.putStr(s"Experiments: $allExperiments\n")

        experiment <- experiments.getById("1")

        _ <- console.putStr(s"Experiment: $experiment\n")

        run <- run.getById("bebec7f85a104ba1b4cd8a8905a74127")

        _ <- console.putStr(s"Run: $run\n")
      } yield (allExperiments, experiment, run)

    }.result

    println(clientResult)
  }

}
