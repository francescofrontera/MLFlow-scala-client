package io.github.francescofrontera.client.services

import io.github.francescofrontera.client.internal.{ ClientCall, InternalClient }
import io.github.francescofrontera.models.{ Run, RunError }
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

    def runService: Service[InternalClient] = new RunService.Service[InternalClient] {
      def getById(runId: String): RunResult[InternalClient, Run] =
        ZIO.accessM[InternalClient](_.internalClient.getClient) flatMap { cli =>
          import cli._

          ClientCall
            .genericGet[Run](
              URLUtils.makeURL(
                basePath = cli.url,
                pathParameters = RunURL ++ Seq("get"),
                queryParameters = Map("run_id" -> runId)
              )
            )
            .mapError(error => RunError(error.getMessage))
        }
    }
  }

  object Live extends Live
}
