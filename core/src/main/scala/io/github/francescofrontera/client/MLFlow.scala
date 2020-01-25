package io.github.francescofrontera.client

import io.github.francescofrontera.client.services.ExperimentService.ExperimentServiceImpl
import io.github.francescofrontera.client.services.RunService.RunServiceImpl
import io.github.francescofrontera.client.services.{ ExperimentService, RunService }
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio._
import zio.console.Console

object MLFlow {
  type MLFLowResult[+A] = IO[Throwable, A]
  type Fun[OUT]         = AllService => MLFLowResult[OUT]

  sealed trait AllService extends ExperimentService with RunService with Console.Live

  def apply[OUT](mlflowURL: String)(f: Fun[OUT]): MLFLowResult[OUT] =
    for {
      client <- AsyncHttpClientZioBackend()
      env = new AllService {
        implicit val be = client

        def experimentService: ExperimentService.Service = new ExperimentServiceImpl(mlflowURL)
        def runService: RunService.Service               = new RunServiceImpl(mlflowURL)
      }
      action <- ZIO.accessM[AllService](services => f(services).ensuring(client.close().ignore)).provide(env)
    } yield action

}
