package io.github.francescofrontera.services

import io.github.francescofrontera.models.{ ClientConfig, Run }
import io.github.francescofrontera.services.utils.{ Dispatch, EncodeDecode }
import zio.Task

trait RunService {
  def runService: RunService.Service
}

object RunService {
  trait Service {
    def getById(runId: String): Task[Run]
  }

  abstract class RunServiceImpl(clientConf: ClientConfig) extends RunService.Service {
    val mlflowURL = clientConf.mlflowURL

    private[this] val RunURL: Seq[String] = "runs" +: Nil

    override def getById(runId: String): Task[Run] =
      for {
        responseAsString <- Dispatch.callGet(pathParameters = RunURL ++ Seq("get"),
                                             basePath = mlflowURL,
                                             queryParameters = Map("run_id" -> runId))
        run <- Task.fromTry(EncodeDecode.decode[Run](responseAsString))
      } yield run
  }

}
