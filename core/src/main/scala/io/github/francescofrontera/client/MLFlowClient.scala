package io.github.francescofrontera.client

import io.github.francescofrontera.client.internal.InternalClient
import io.github.francescofrontera.client.services.AllServiceLive.AllServiceT
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio._

case class MLFlowClient(mlflowURL: String) {
  type MLFLowResult[+A]  = IO[Throwable, A]
  type ClientResult[OUT] = ZIO[AllServiceT, Throwable, OUT]

  type Fun[OUT] = AllServiceT => RIO[InternalClient, OUT]

  final def allService[OUT](f: Fun[OUT]): ClientResult[OUT] =
    AsyncHttpClientZioBackend().toManaged(_.close().orDie) use { implicit cli =>
      ZIO.accessM[AllServiceT](f(_).provide(InternalClient.Live(mlflowURL)))
    }
}
