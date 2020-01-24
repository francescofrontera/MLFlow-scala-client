package io.github.francescofrontera.client

import io.github.francescofrontera.client.services.ExperimentService.ExperimentServiceImpl
import io.github.francescofrontera.client.services.RunService.RunServiceImpl
import io.github.francescofrontera.client.services.{ ExperimentService, RunService }
import zio._

object MLFlow {
  type MLFLowResult[+A] = IO[Throwable, A]
  type FnInFnOut[OUT]   = (ExperimentService.Service, RunService.Service) => Task[OUT]

  trait Default extends InternalClient

  private[this] object Live extends Default {
    def sttp: InternalClient.Serve = InternalClient.SttpMaterialization
  }

  def apply[OUT](mlflowURL: String)(f: FnInFnOut[OUT]): MLFLowResult[OUT] =
    ZIO.accessM[Default](_.sttp.taskClient) flatMap { implicit taskClient =>
      val experimentService = new ExperimentServiceImpl(mlflowURL)
      val runService        = new RunServiceImpl(mlflowURL)

      f(experimentService, runService).ensuring(taskClient.close().ignore)
    } provide Live

}
