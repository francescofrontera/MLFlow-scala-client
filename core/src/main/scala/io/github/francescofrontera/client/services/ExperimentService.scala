package io.github.francescofrontera.client.services

import io.github.francescofrontera.client.internal.InternalClient.InternalClient
import io.github.francescofrontera.models.Experiment._
import io.github.francescofrontera.models._
import zio._

object ExperimentService {
  type ExperimentService = Has[ExperimentService.Service]

  trait Service {
    def getAll: Task[Experiments]
    def getById(id: String): Task[Experiment]
    def create(experiment: ExperimentObject): Task[ExperimentResponse]
  }

  private[this] final val ExperimentPath = "experiments" +: Nil

  val live: ZLayer[InternalClient, Nothing, ExperimentService] = ZLayer.fromFunction(
    ic =>
      new ExperimentService.Service {
        def create(experiment: ExperimentObject): Task[ExperimentResponse] =
          ic.get
            .genericPost[ExperimentObject, ExperimentResponse](
              makePath("create"),
              experiment
            )
            .mapError(error => ExperimentsError(error.getMessage))

        def getAll: Task[Experiments] =
          ic.get
            .genericGet[Experiments](ExperimentPath ++ Seq("list"))
            .mapError(error => ExperimentsError(error.getMessage))

        def getById(id: String): Task[Experiment] =
          ic.get
            .genericGet[Experiment](makePath("get"))
            .mapError(error => ExperimentsError(error.getMessage))
    }
  )

  private[this] def makePath(add: String): Seq[String] = ExperimentPath ++ Seq(add)
}
