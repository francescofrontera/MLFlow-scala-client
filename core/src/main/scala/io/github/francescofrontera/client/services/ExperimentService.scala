package io.github.francescofrontera.client.services

import io.github.francescofrontera.client.internal._
import io.github.francescofrontera.models._
import io.github.francescofrontera.utils.URLUtils
import zio._

trait ExperimentService {
  def experimentService: ExperimentService.Service[InternalClient]
}

object ExperimentService {
  type ExperimentResult[R, +A] = RIO[R, A]

  trait Service[R] {
    def getAll: ExperimentResult[R, Experiments]
    def getById(id: String): ExperimentResult[R, Experiment]
  }

  trait Live extends ExperimentService {
    private[this] val ExperimentPath = "experiments" +: Nil

    def experimentService: Service[InternalClient] = new ExperimentService.Service[InternalClient] {
      def getAll: ExperimentResult[InternalClient, Experiments] =
        for {
          ic  <- ZIO.access[InternalClient](_.internalClient)
          url <- ic.url
          call <- ic
            .genericGet[Experiments](
              URLUtils.makeURL(
                pathParameters = ExperimentPath ++ Seq("list"),
                basePath = url
              )
            )
            .mapError(error => ExperimentsError(error.getMessage))
        } yield call

      def getById(id: String): ExperimentResult[InternalClient, Experiment] =
        for {
          ic  <- ZIO.access[InternalClient](_.internalClient)
          url <- ic.url
          call <- ic
            .genericGet[Experiment](
              URLUtils.makeURL(basePath = url,
                               pathParameters = ExperimentPath ++ Seq("get"),
                               queryParameters = Map("experiment_id" -> id))
            )
            .mapError(error => ExperimentsError(error.getMessage))
        } yield call

    }
  }

  object Live extends Live
}
