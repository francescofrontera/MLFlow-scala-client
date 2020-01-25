import io.github.francescofrontera.client.MLFlow
import io.github.francescofrontera.client.MLFlow.MLFLowResult
import zio.{ DefaultRuntime, ZIO }

object TryServices extends DefaultRuntime {
  def main(args: Array[String]): Unit = {

    val define: MLFLowResult[List[Product]] =
      MLFlow("http://localhost:5000/api/2.0/preview/mlflow")(ZIO.accessM[MLFlow.AllService] { modules =>
        val experimentes = modules.experimentService
        val run          = modules.runService

        for {
          allExperiments <- experimentes.getAll
          byId           <- experimentes.getById("1")
          run            <- run.getById("231e8ac802ed42a8b807174f1d9e9501")
        } yield List(allExperiments, byId, run)
      })

    val result = unsafeRun(define)

    println(result.mkString("\n"))
  }
}
