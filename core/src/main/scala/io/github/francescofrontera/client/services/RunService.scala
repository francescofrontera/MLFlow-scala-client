package io.github.francescofrontera.client.services

import io.github.francescofrontera.client.internal.InternalClient
import io.github.francescofrontera.models.Run
import io.github.francescofrontera.utils.URLUtils
import zio.{ RIO, ZIO }

trait RunService {
  def runService: RunService.Service[InternalClient]
}

object RunService {
  type RunResult[R, +A] = RIO[R, A]

  trait Service[R] {
    def getById(runId: String): RunResult[R, Run]
  }

  trait Live extends RunService {
    private[this] val RunURL: Seq[String] = "runs" +: Nil

    def runService: Service[InternalClient] =
      (runId: String) =>
        for {
          ic  <- ZIO.access[InternalClient](_.internalClient)
          url <- ic.url
          call <- ic
            .genericGet[Run](
              URLUtils.makeURL(
                basePath = url,
                pathParameters = RunURL ++ Seq("get"),
                queryParameters = Map("run_id" -> runId)
              )
            )
        } yield call
  }

  object Live extends Live
}
