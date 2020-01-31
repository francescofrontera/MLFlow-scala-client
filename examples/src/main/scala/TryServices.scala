import io.github.francescofrontera.client.MLFlowClient
import io.github.francescofrontera.models.{ Experiment, Run }

object TryServices
  def main(args: Array[String]): Unit = {
    val clientResult: (Experiment, Run) =
      MLFlowClient("http://localhost:5000/api/2.0/preview/mlflow").unsafeRun { ser =>
        for {
          one <- ser.experimentService.getById("1")
          two <- ser.runService.getById("bebec7f85a104ba1b4cd8a8905a74127")
        } yield (one, two)
      }

    println(clientResult)
  }
