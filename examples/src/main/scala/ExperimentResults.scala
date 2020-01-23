import io.github.francescofrontera.client.MLFlow
import io.github.francescofrontera.client.executor.Executor._

/*
  Note: An instance of MLFlow tracking service is required to execute the program.
 */
object ExperimentResults extends App {
  val experiments = for {
    mlflowClient <- MLFlow()

    experiments <- mlflowClient.experiment
    mlrun       <- mlflowClient.run

    //Action over client
    allExperiments <- experiments.getByExperimentId("1")
    runById        <- mlrun.getById(allExperiments.experiment.experiment_id)
  } yield allExperiments

  val result = experiments.asEither

  println(result)

  sys.exit(0)
}
