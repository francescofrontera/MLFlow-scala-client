package io.github.francescofrontera.client.services

import io.github.francescofrontera.models._
import io.github.francescofrontera.utils.URLUtils
import sttp.client._
import sttp.client.asynchttpclient.WebSocketHandler
import sttp.client.circe._
import sttp.model.Uri
import zio.Task

trait ExperimentService {
  def experimentService: ExperimentService.Service
}

object ExperimentService {
  type ExperimentResult[+A] = Task[A]

  trait Service {
    def getAll: ExperimentResult[Experiments]
    def getById(id: String): ExperimentResult[Experiment]
  }

  class ExperimentServiceImpl(mlflowURL: String)(implicit be: SttpBackend[Task, Nothing, WebSocketHandler])
      extends ExperimentService.Service {
    private[this] val ExperimentPath = "experiments" +: Nil

    def getAll: ExperimentResult[Experiments] = {
      val url = Uri(URLUtils.makeURL(ExperimentPath ++ Seq("list"), mlflowURL))

      //FIXME: Refactoring this block..
      for {
        jsonResult <- basicRequest.get(url).response(asJson[Experiments]).send()
        value      <- Task.fromEither(jsonResult.body)
      } yield value
    }

    override def getById(id: String): ExperimentResult[Experiment] = {
      val url = URLUtils.makeURL(pathParameters = ExperimentPath ++ Seq("get"),
                                 basePath = mlflowURL,
                                 queryParameters = Map("experiment_id" -> id))

      for {
        jsonResult <- basicRequest.get(Uri(url)).response(asJson[Experiment]).send()
        experiment <- Task.fromEither(jsonResult.body)
      } yield experiment
    }
  }
}
