import io.github.francescofrontera.client.runner.MLFlowCRunner
import io.github.francescofrontera.client.services._
import zio.{App, ZIO}

object TryServices {
  def main(args: Array[String]): Unit = {
    MLFlowCRunner("http://localhost:5000/api/2.0/preview/mlflow").call(getAllExperimentService)
  }
}
