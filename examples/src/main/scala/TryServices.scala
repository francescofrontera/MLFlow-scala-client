import io.github.francescofrontera.client.MLFlowClient
import io.github.francescofrontera.client.runner.MLFlowDefaultRunner
import io.github.francescofrontera.client.services.AllServiceLive.AllService
import io.github.francescofrontera.models.{ Experiment, Run }
import zio.ZIO

object TryServices extends MLFlowDefaultRunner {
  def main(args: Array[String]): Unit = {
    val program: ZIO[AllService, Throwable, (Experiment, Run)] =
      MLFlowClient("http://localhost:5000/api/2.0/preview/mlflow") allService { ser =>
        for {
          one <- ser.experimentService.getById("1")
          two <- ser.runService.getById("bebec7f85a104ba1b4cd8a8905a74127")
        } yield (one, two)
      }

    println(execute.unsafeRun(program))
  }
}
