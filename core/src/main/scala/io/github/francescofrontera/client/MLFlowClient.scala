package io.github.francescofrontera.client

import io.github.francescofrontera.client.internal.InternalClient
import io.github.francescofrontera.client.services.AllServiceLive.AllService
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio._

case class MLFlowClient(mlflowURL: String) {
  type MLFLowResult[+A] = IO[Throwable, A]
  type Fun[OUT]         = AllService => RIO[InternalClient, OUT]

  final def allService[OUT](f: Fun[OUT]): ZIO[AllService, Throwable, OUT] =
    AsyncHttpClientZioBackend().toManaged(_.close().orDie) use { implicit cli =>
      ZIO.accessM[AllService] { f(_).provide(InternalClient.Live(mlflowURL)) }
    }
}
