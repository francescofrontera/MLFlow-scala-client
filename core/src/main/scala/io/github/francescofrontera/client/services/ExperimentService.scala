package io.github.francescofrontera.client.services

import io.github.francescofrontera.client.{ ClientCall, InternalClient }
import io.github.francescofrontera.models._
import io.github.francescofrontera.utils.URLUtils
import sttp.model.Uri
import zio.{ RIO, Task }

trait ExperimentService {
  def experimentService: ExperimentService.Service[Any]
}

object ExperimentService {
  type ExperimentResult[R, +A] = RIO[R, A]

  trait Service[R] {
    def getAll: ExperimentResult[R, Experiments]
    def getById(id: String): ExperimentResult[R, Experiment]
  }

  class ExperimentServiceImpl(mlflowURL: String)(implicit be: InternalClient) extends ExperimentService.Service[Any] {
    private[this] val ExperimentPath = "experiments" +: Nil

    final def getAll: Task[Experiments] =
      ClientCall.genericGet[Experiments](
        Uri(URLUtils.makeURL(ExperimentPath ++ Seq("list"), mlflowURL))
      )

    final def getById(id: String): Task[Experiment] =
      ClientCall.genericGet[Experiment](
        Uri(
          URLUtils.makeURL(pathParameters = ExperimentPath ++ Seq("get"),
                           basePath = mlflowURL,
                           queryParameters = Map("experiment_id" -> id))
        )
      )
  }
}
