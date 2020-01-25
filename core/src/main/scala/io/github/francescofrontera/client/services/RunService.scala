package io.github.francescofrontera.client.services

import io.github.francescofrontera.models.Run
import io.github.francescofrontera.utils.URLUtils
import sttp.client.asynchttpclient.WebSocketHandler
import sttp.client.circe._
import sttp.client.{ basicRequest, SttpBackend }
import sttp.model.Uri
import zio.Task

trait RunService {
  def runService: RunService.Service
}

object RunService {
  trait Service {
    def getById(runId: String): Task[Run]
  }

  class RunServiceImpl(mlflowURL: String)(implicit be: SttpBackend[Task, Nothing, WebSocketHandler])
      extends RunService.Service {
    private[this] val RunURL: Seq[String] = "runs" +: Nil

    override def getById(runId: String): Task[Run] = {
      val url = URLUtils.makeURL(RunURL ++ Seq("get"), mlflowURL, Map("run_id" -> runId))

      for {
        jsonResult <- basicRequest.get(Uri(url)).response(asJson[Run]).send()
        run        <- Task.fromEither(jsonResult.body)
      } yield run
    }
  }
}
