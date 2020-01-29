import io.github.francescofrontera.client.MLFlowClient
import io.github.francescofrontera.client.Executor._
import io.github.francescofrontera.models.Experiments

object TryServices {

  def main(args: Array[String]): Unit = {
    val clientResult: Either[Throwable, Experiments] =
      MLFlowClient("http://localhost:5000/api/2.0/preview/mlflow")(_.experimentService.getAll).result

    println(clientResult)
  }

}
