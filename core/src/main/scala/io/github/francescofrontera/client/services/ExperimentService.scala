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
        ZIO.accessM[InternalClient](_.internalClient.getClient) flatMap { cli =>
          import cli._

          ClientCall
            .genericGet[Experiments](
              URLUtils.makeURL(
                pathParameters = ExperimentPath ++ Seq("list"),
                basePath = cli.url
              )
            )
            .mapError(error => ExperimentsError(error.getMessage))
        }

      def getById(id: String): ExperimentResult[InternalClient, Experiment] =
        ZIO.accessM[InternalClient](_.internalClient.getClient) flatMap { cli =>
          import cli._

          ClientCall
            .genericGet[Experiment](
              URLUtils.makeURL(basePath = cli.url,
                               pathParameters = ExperimentPath ++ Seq("get"),
                               queryParameters = Map("experiment_id" -> id))
            )
        }
    }
  }

  object Live extends Live
}
