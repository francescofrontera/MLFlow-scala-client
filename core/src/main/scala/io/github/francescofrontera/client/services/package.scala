package io.github.francescofrontera.client

import io.github.francescofrontera.client.services.ExperimentService.ExperimentService
import io.github.francescofrontera.client.services.RunService.RunService
import io.github.francescofrontera.models.Experiment.ExperimentObject
import io.github.francescofrontera.models.{ Experiment, ExperimentResponse, Experiments }
import zio.ZIO

package object services {
  type ALLSERV = ExperimentService with RunService

  final val AllService = ExperimentService.live ++ RunService.live

  def getAllExperimentService: ZIO[ExperimentService, Throwable, Experiments] =
    ZIO.accessM[ExperimentService](s => s.get.getAll)
  def getExperimentById(id: String): ZIO[ExperimentService, Throwable, Experiment] =
    ZIO.accessM[ExperimentService](s => s.get.getById(id))
  def createExperiment(in: ExperimentObject): ZIO[ExperimentService, Throwable, ExperimentResponse] =
    ZIO.accessM[ExperimentService](s => s.get.create(in))
}
