package io.github.francescofrontera.client.services

import io.github.francescofrontera.client._
import io.github.francescofrontera.models._
import io.github.francescofrontera.utils.URLUtils
import zio._

trait ExperimentService {
  def experimentService: ExperimentService.Service[Any]
}

object ExperimentService {
  type ExperimentResult[R, +A] = RIO[R, A]

  trait Service[R] {
    def getAll: ExperimentResult[R, Experiments]
    def getById(id: String): ExperimentResult[R, Experiment]
  }

  final class ExperimentServiceImpl(mlflowURL: String)(implicit be: InternalClient)
      extends ExperimentService.Service[Any] {
    private[this] val ExperimentPath = "experiments" +: Nil

    def getAll: Task[Experiments] =
      ClientCall
        .genericGet[Experiments](
          URLUtils.makeURL(
            pathParameters = ExperimentPath ++ Seq("list"),
            basePath = mlflowURL
          )
        )
        .mapError(error => ExperimentsError(error.getMessage))

    def getById(id: String): Task[Experiment] =
      ClientCall
        .genericGet[Experiment](
          URLUtils.makeURL(basePath = mlflowURL,
                           pathParameters = ExperimentPath ++ Seq("get"),
                           queryParameters = Map("experiment_id" -> id))
        )
        .mapError(error => ExperimentsError(error.getMessage))
  }
}
