import io.github.francescofrontera.client.runner.MLFlowCRunner
import io.github.francescofrontera.client.services._

object TryServices {
  final val MLFlowURL = "http://localhost:5000/api/2.0/preview/mlflow"

  def main(args: Array[String]): Unit =
    MLFlowCRunner(MLFlowURL).call(getAllExperimentService)
}
