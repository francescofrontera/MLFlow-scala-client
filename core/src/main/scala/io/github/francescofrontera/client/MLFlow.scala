package io.github.francescofrontera.client

import io.github.francescofrontera.client.services.ExperimentService.ExperimentServiceImpl
import io.github.francescofrontera.client.services.RunService.RunServiceImpl
import io.github.francescofrontera.client.services.{ ExperimentService, RunService }
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio._

object MLFlow {
  type MLFLowResult[+A] = IO[Throwable, A]
  type FnInFnOut[OUT]   = (ExperimentService with RunService) => Task[OUT]

  trait Default extends InternalClient

  private[this] object Live extends Default {
    def sttp: InternalClient.Serve = InternalClient.SttpMaterialization
  }

  def apply[OUT](mlflowURL: String)(f: FnInFnOut[OUT]): MLFLowResult[OUT] =
    for {
      client <- AsyncHttpClientZioBackend()
      env = new ExperimentService with RunService {
        implicit val be = client

        def experimentService: ExperimentService.Service = new ExperimentServiceImpl(mlflowURL)
        def runService: RunService.Service               = new RunServiceImpl(mlflowURL)
      }
      action <- f(env).ensuring(client.close().ignore)
    } yield action

}
