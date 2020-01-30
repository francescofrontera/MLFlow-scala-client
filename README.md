# MLFlow-scala-client
MLFlow-scala-client is a fresh made, pure functional, wrapper around MFLow tracking server API.

### Idea 
```scala 
val clientResult: Either[Throwable, Experiments] =
      MLFlowClient("http://localhost:5000/api/2.0/preview/mlflow")(_.experimentService.getAll).result

clientResult: Either[Throwable, Experiments] = Right(
    Experiments(
        List(
            ExperimentObject(
                0,
                Default,
                file:///Users/xxx/PycharmProjects/example/mlruns/0,
                active
            ),
             ExperimentObject(
                1,
                3countries_fb_training,
                file:///Users/xxx/PycharmProjects/example/mlruns/1,
                active
            )
        )
    )
)
```
