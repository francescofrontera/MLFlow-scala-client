import io.github.francescofrontera.client.MLFlow
import io.github.francescofrontera.client.executor.Executor._
import io.github.francescofrontera.models.{ Experiment, Experiments }

/*
  Note: An instance of MLFlow tracking service is required to execute the program.
 */

object ExperimentResults extends App {
  val experiments = for {
    mlflowClient <- MLFlow()
    experiments  <- mlflowClient.experiment

    //Action over client
    allExperiments <- experiments.getAll
    singleExpById  <- experiments.getByExperimentId("1")
  } yield (allExperiments, singleExpById)

  val result: Either[Throwable, (Experiments, Experiment)] = experiments.asEither

  sys.exit(1)
}
