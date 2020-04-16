package io.github.francescofrontera.client.services

import io.github.francescofrontera.client.internal.InternalClient.InternalClient
import io.github.francescofrontera.models.Run
import zio._

object RunService {
  type RunService = Has[RunService.Service]

  trait Service {
    def getById(runId: String): Task[Run]
  }

  private[this] val RunURL: Seq[String] = "runs" +: Nil
  val live: ZLayer[InternalClient, Nothing, RunService] = ZLayer.fromFunction(
    ic =>
      (runId: String) =>
        ic.get.genericGet[Run](RunURL ++ Seq("get"), Map("run_id" -> runId))
  )
}
