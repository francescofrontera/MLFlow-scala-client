package io.github.francescofrontera.client

import io.github.francescofrontera.configuration.ClientConfiguration
import io.github.francescofrontera.configuration.ClientConfiguration.AppConf
import io.github.francescofrontera.services.ExperimentService.ExperimentServiceImpl
import io.github.francescofrontera.services.RunService.RunServiceImpl
import io.github.francescofrontera.services.{ ExperimentService, RunService }
import zio._

trait MLFlow {
  def builder: MLFlow.Build
}

object MLFlow {
  type MLFlowService[+A] = Task[A]
  type MLFlowBuilder[+A] = IO[Nothing, A]

  trait Build {
    def clientConfiguration: ClientConfiguration.Service

    def experiment: MLFlowService[ExperimentService.Service]
    def run: MLFlowService[RunService.Service]
  }

  lazy val default = new MLFlow {
    val builder: Build = new Build {
      def clientConfiguration: ClientConfiguration.Service = new AppConf

      def experiment: MLFlowService[ExperimentService.Service] =
        clientConfiguration.materializeConfig.map(new ExperimentServiceImpl(_) {})

      def run: MLFlowService[RunService.Service] =
        clientConfiguration.materializeConfig.map(new RunServiceImpl(_) {})
    }
  }

  def apply(): MLFlowBuilder[Build] =
    ZIO
      .accessM[MLFlow](modules => UIO.succeed(modules.builder))
      .provide(default)

}
