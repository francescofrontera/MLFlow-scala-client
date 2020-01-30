package io.github.francescofrontera.client

import io.github.francescofrontera.client.services.ExperimentService.ExperimentServiceImpl
import io.github.francescofrontera.client.services.RunService.RunServiceImpl
import io.github.francescofrontera.client.services.{ ExperimentService, RunService }
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio._
import zio.console.Console

object MLFlowClient {
  type MLFLowResult[+A] = IO[Throwable, A]
  type Fun[OUT]         = ClientLive => MLFLowResult[OUT]

  sealed trait ClientLive extends ExperimentService with RunService with Console.Live

  def apply[OUT](mlflowURL: String)(f: Fun[OUT]): MLFLowResult[OUT] =
    AsyncHttpClientZioBackend().toManaged(_.close().orDie) use { implicit cli =>
      val materializedCLive = new ClientLive {
        def experimentService = new ExperimentServiceImpl(mlflowURL)
        def runService        = new RunServiceImpl(mlflowURL)
      }

      ZIO.accessM[ClientLive](f).provide(materializedCLive)
    }

}
