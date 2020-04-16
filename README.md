[![<ORG_NAME>](https://circleci.com/gh/francescofrontera/MLFlow-scala-client.svg?style=svg)](https://circleci.com/gh/francescofrontera/MLFlow-scala-client)

# MLFlow-scala-client
MLFlow-scala-client is a fresh made, pure functional, wrapper around MFLow tracking server API.

### Idea 
```scala 
object TryServices extends MLFlowDefaultRunner {
  def main(args: Array[String]): Unit = {
      MLFlowCRunner("http://localhost:5000/api/2.0/preview/mlflow")
        .call(getAllExperimentService)
    }
}
```
