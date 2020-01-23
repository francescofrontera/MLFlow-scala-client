import io.github.francescofrontera.client.MLFlow
import io.github.francescofrontera.client.executor.Executor._

object RunResults extends App {
  val runs = for {
    mlFlowClient <- MLFlow()
    runs         <- mlFlowClient.run

    runById <- runs.getById("231e8ac802ed42a8b807174f1d9e9501") //TODO: Using real run id
  } yield runById

  val result = runs.asEither

  println(result)

  sys.exit(0)
}
