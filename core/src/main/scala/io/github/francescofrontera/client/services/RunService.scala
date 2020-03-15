package io.github.francescofrontera.client.services

import io.github.francescofrontera.client.internal.InternalClient.InternalClient
import io.github.francescofrontera.models.Run
import io.github.francescofrontera.utils.URLUtils
import zio._

object RunService {
  type RunService = Has[RunService.Service]

  trait Service {
    def getById(runId: String): Task[Run]
  }

  private[this] val RunURL: Seq[String] = "runs" +: Nil
  val live: ZLayer[InternalClient, Nothing, RunService] = ZLayer.fromFunction(
    ic =>
      new Service {
        def getById(runId: String): Task[Run] =
          for {
            url <- ic.get.url
            call <- ic.get.genericGet[Run](
              URLUtils.makeURL(
                basePath = url,
                pathParameters = RunURL ++ Seq("get"),
                queryParameters = Map("run_id" -> runId)
              )
            )
          } yield call
    }
  )
}
