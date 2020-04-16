package io.github.francescofrontera.client.runner

import io.github.francescofrontera.client.internal.InternalClient
import io.github.francescofrontera.client.services.ExperimentService.ExperimentService
import io.github.francescofrontera.client.services.RunService.RunService
import io.github.francescofrontera.client.services._
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.{Runtime, ZIO, ZLayer}

sealed case class MLFlowCRunner(mlflowURL: String) {
  private[this] val execute = Runtime.default

  private[this] val cInternal =
    ZLayer.fromManaged(AsyncHttpClientZioBackend().toManaged(_.close().orDie)) >>>
      InternalClient.live(mlflowURL) >>>
      AllService

  def call[OUT](action: ZIO[ExperimentService with RunService, Throwable, OUT]): OUT =
    execute.unsafeRun(action.provideLayer(cInternal))

}
