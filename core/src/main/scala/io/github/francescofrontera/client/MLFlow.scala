package io.github.francescofrontera.client

import io.github.francescofrontera.client.services.ExperimentService.ExperimentServiceImpl
import io.github.francescofrontera.client.services.RunService.RunServiceImpl
import io.github.francescofrontera.client.services.{ ExperimentService, RunService }
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio._

object MLFlow {
  type MLFLowResult[+A] = IO[Throwable, A]

  trait AllService extends ExperimentService with RunService

  def apply[OUT](mlflowURL: String)(f: ZIO[AllService, Throwable, OUT]): MLFLowResult[OUT] =
    for {
      client <- AsyncHttpClientZioBackend()
      env = new AllService {
        implicit val be = client

        def experimentService: ExperimentService.Service = new ExperimentServiceImpl(mlflowURL)
        def runService: RunService.Service               = new RunServiceImpl(mlflowURL)
      }
      action <- f.ensuring(client.close().ignore).provide(env)
    } yield action

}
