import io.github.francescofrontera.client.MLFlowClient
import io.github.francescofrontera.client.runner.MLFlowDefaultRunner
import io.github.francescofrontera.models._, Experiment._

object TryServices {
  val runner = new MLFlowDefaultRunner {}

  def main(args: Array[String]): Unit = {
    import runner._

    val program: MLFlowClient#ClientResult[(Experiment, Run, ExperimentResponse)] =
      MLFlowClient("http://localhost:5000/api/2.0/preview/mlflow") allService { ser =>
        val experiment = ser.experimentService
        val run        = ser.runService

        for {
          exp <- experiment.create(ExperimentObject(name = "Home"))
          one <- experiment.getById("1")
          two <- run.getById("bebec7f85a104ba1b4cd8a8905a74127")
        } yield (one, two, exp)
      }

    println(program.result)
  }
}
