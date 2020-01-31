package io.github.francescofrontera.client

import io.github.francescofrontera.client.internal.InternalClient
import io.github.francescofrontera.client.services.{ ExperimentService, RunService }
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio._
import zio.internal.PlatformLive

import scala.concurrent.Future

case class MLFlowClient(mlflowURL: String) {
  type AllService       = ExperimentService.Live with RunService.Live
  type MLFLowResult[+A] = IO[Throwable, A]
  type Fun[OUT]         = AllService => RIO[InternalClient, OUT]

  sealed trait ClientLive extends ExperimentService.Live with RunService.Live

  final val runner = Runtime(new ClientLive {}, PlatformLive.Default)

  private[this] def run[OUT](f: Fun[OUT]): ZIO[AllService, Throwable, OUT] =
    AsyncHttpClientZioBackend().toManaged(_.close().orDie) use { cli =>
      ZIO.accessM[AllService] { f(_).provide(InternalClient.Live(mlflowURL, cli)) }
    }

  //Executor Methods..
  @inline def unsafeRun[OUT](f: Fun[OUT]): OUT        = runner.unsafeRun(run[OUT](f))
  @inline def asyncRun[OUT](f: Fun[OUT]): Future[OUT] = runner.unsafeRunToFuture(run[OUT](f))
}
