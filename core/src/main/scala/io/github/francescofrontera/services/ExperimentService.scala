package io.github.francescofrontera.services

import io.github.francescofrontera.models._
import io.github.francescofrontera.services.utils.{ Dispatch, EncodeDecode }
import zio._

trait ExperimentService {
  def experimentService: ExperimentService.Service
}

object ExperimentService {
  type ExperimentResult[+A] = Task[A]

  trait Service {
    def getAll: ExperimentResult[Experiments]
    def getByExperimentId(id: String): ExperimentResult[Experiment]
  }

  abstract class ExperimentServiceImpl(clientConf: ClientConfig) extends ExperimentService.Service {
    private[this] val ExperimentPath = "experiments" +: Nil
    private[this] val mlflowURL      = clientConf.mlflowURL

    def getAll: ExperimentResult[Experiments] =
      for {
        responseAsStr <- Dispatch.callGet(ExperimentPath ++ Seq("list"), mlflowURL)
        task          <- Task.fromTry(EncodeDecode.decode[Experiments](responseAsStr))
      } yield task

    def getByExperimentId(id: String): ExperimentResult[Experiment] =
      for {
        responseAsString <- Dispatch.callGet(pathParameters = ExperimentPath ++ Seq("get"),
                                             basePath = mlflowURL,
                                             queryParameters = Map("experiment_id" -> id))

        task <- Task.fromTry(EncodeDecode.decode[Experiment](responseAsString))
      } yield task
  }

}
