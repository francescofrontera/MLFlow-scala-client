package io.github.francescofrontera.client.services

import io.github.francescofrontera.client.{ ClientCall, InternalClient }
import io.github.francescofrontera.models.{ Run, RunError }
import io.github.francescofrontera.utils.URLUtils
import zio.{ RIO, Task }

trait RunService {
  def runService: RunService.Service[Any]
}

object RunService {
  type RunResult[R, +A] = RIO[R, A]

  trait Service[R] {
    def getById(runId: String): RunResult[R, Run]
  }

  final class RunServiceImpl(mlflowURL: String)(implicit be: InternalClient) extends RunService.Service[Any] {
    type RunTask[+A] = Task[A]

    private[this] val RunURL: Seq[String] = "runs" +: Nil

    def getById(runId: String): RunTask[Run] =
      ClientCall
        .genericGet[Run](
          URLUtils.makeURL(
            basePath = mlflowURL,
            pathParameters = RunURL ++ Seq("get"),
            queryParameters = Map("run_id" -> runId)
          )
        )
        .mapError(error => RunError(error.getMessage))
  }
}
