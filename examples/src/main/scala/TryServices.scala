import io.github.francescofrontera.client.MLFlow
import zio._

object TryServices extends App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    MLFlow("http://localhost:5000/api/2.0/preview/mlflow")(ZIO.accessM[MLFlow.AllService] { modules =>
      val experimentes = modules.experimentService
      val run          = modules.runService

      for {
        allExperiments <- experimentes.getAll
        _              <- modules.console.putStr(s"ALL EXPERIMENTS: $allExperiments\n")
        byId           <- experimentes.getById("1")
        _              <- modules.console.putStr(s"Experiment: $byId\n")
        run            <- run.getById("231e8ac802ed42a8b807174f1d9e9501")
        _              <- modules.console.putStr(s"Run: $run\n")
      } yield List(allExperiments, byId, run)
    }).fold(_ => 0, _ => 1)

}
