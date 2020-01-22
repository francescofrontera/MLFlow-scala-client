package io.github.francescofrontera.client

import io.github.francescofrontera.configuration.ClientConfiguration
import io.github.francescofrontera.configuration.ClientConfiguration.AppConf
import io.github.francescofrontera.services.ExperimentService
import zio._

trait MLFlow {
  def builder: MLFlow.Build
}

object MLFlow {
  type MLFlowResult[+A] = Task[A]

  trait Build {
    def clientConfiguration: ClientConfiguration.Service
    def experiment: MLFlowResult[ExperimentService.Service]
  }

  trait Default extends Build {
    val clientConfiguration: ClientConfiguration.Service = new AppConf

    def experiment: MLFlowResult[ExperimentService.Service] =
      for {
        clientConf        <- clientConfiguration.materializeConfig
        experimentService <- Task.succeed(ExperimentService(clientConf))
        //FIXME: ALL SERVICE
      } yield experimentService
  }

  val default = new MLFlow { val builder: Build = new Default {} }

  def apply(): IO[Nothing, Build] =
    ZIO
      .accessM[MLFlow](modules => UIO.succeed(modules.builder))
      .provide(default)

}
