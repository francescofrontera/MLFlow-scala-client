[![<ORG_NAME>](https://circleci.com/gh/francescofrontera/MLFlow-scala-client.svg?style=svg)](https://circleci.com/gh/francescofrontera/MLFlow-scala-client)

# MLFlow-scala-client
MLFlow-scala-client is a fresh made, pure functional, wrapper around MFLow tracking server API.

### Idea 
```scala 
object TryServices extends MLFlowDefaultRunner {
  def main(args: Array[String]): Unit = {
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
```
