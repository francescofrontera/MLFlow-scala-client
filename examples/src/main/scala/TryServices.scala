import io.github.francescofrontera.client.MLFlowClient
import zio._

object TryServices extends App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    MLFlowClient("http://localhost:5000/api/2.0/preview/mlflow") { modules =>
      val (experiments, run, console) = (
        modules.experimentService,
        modules.runService,
        modules.console
      )

      for {
        allExperiments <- experiments.getAll

        _ <- console.putStr(s"ALL EXPERIMENTS: $allExperiments\n")

        byId <- experiments.getById("1")

        _ <- console.putStr(s"Experiment: $byId\n")

        run <- run.getById("231e8ac802ed42a8b807174f1d9e9501")

        _ <- console.putStr(s"Run: $run\n")

      } yield List(allExperiments, byId, run)
    }.fold(_ => 1, _ => 0)
}
