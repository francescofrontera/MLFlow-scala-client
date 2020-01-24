import io.github.francescofrontera.client.MLFlow
import io.github.francescofrontera.client.MLFlow.MLFLowResult
import zio.DefaultRuntime

object TryServices extends DefaultRuntime {
  def main(args: Array[String]): Unit = {

    val program: MLFLowResult[List[Product]] =
      MLFlow("http://localhost:5000/api/2.0/preview/mlflow") { (exService, runService) =>
        for {
          allExperiments <- exService.getAll
          byId           <- exService.getById("1")
          run            <- runService.getById("231e8ac802ed42a8b807174f1d9e9501")
        } yield List(allExperiments, byId, run)
      }

    val result = unsafeRun(program)

    println(result.mkString("\n"))
  }
}
