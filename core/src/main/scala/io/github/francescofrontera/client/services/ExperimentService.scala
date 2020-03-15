package io.github.francescofrontera.client.services

import io.github.francescofrontera.client.internal.InternalClient.InternalClient
import io.github.francescofrontera.client.internal._
import io.github.francescofrontera.models.Experiment._
import io.github.francescofrontera.models._
import io.github.francescofrontera.utils.URLUtils
import zio._

object ExperimentService {
  type ExperimentService = Has[ExperimentService.Service]

  trait Service {
    def getAll: Task[Experiments]
    def getById(id: String): Task[Experiment]
    def create(experiment: ExperimentObject): Task[ExperimentResponse]
  }

  private[this] final val ExperimentPath = "experiments" +: Nil
  //FIXME: Avoid duplication
  val live: ZLayer[InternalClient, Nothing, ExperimentService] = ZLayer.fromFunction(
    ic =>
      new ExperimentService.Service {
        val i = ic
        def create(experiment: ExperimentObject): Task[ExperimentResponse] =
          for {
            bPath <- ic.get.url
            call <- ic.get
              .genericPost[ExperimentObject, ExperimentResponse](
                uri = URLUtils.makeURL(pathParameters = ExperimentPath ++ Seq("create"), basePath = bPath),
                data = experiment
              )
              .mapError(error => ExperimentsError(error.getMessage))
          } yield call

        def getAll: Task[Experiments] =
          for {
            bPath <- ic.get.url
            call <- ic.get
              .genericGet[Experiments](
                URLUtils.makeURL(
                  pathParameters = ExperimentPath ++ Seq("list"),
                  basePath = bPath
                )
              )
              .mapError(error => ExperimentsError(error.getMessage))
          } yield call

        def getById(id: String): Task[Experiment] =
          for {
            bPath <- ic.get.url
            call <- ic.get
              .genericGet[Experiment](
                URLUtils.makeURL(basePath = bPath,
                                 pathParameters = ExperimentPath ++ Seq("get"),
                                 queryParameters = Map("experiment_id" -> id))
              )
              .mapError(error => ExperimentsError(error.getMessage))
          } yield call

    }
  )
}
