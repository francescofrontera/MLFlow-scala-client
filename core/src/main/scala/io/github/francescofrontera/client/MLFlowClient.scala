package io.github.francescofrontera.client

import io.github.francescofrontera.client.services.ExperimentService.ExperimentServiceImpl
import io.github.francescofrontera.client.services.RunService.RunServiceImpl
import io.github.francescofrontera.client.services.{ ExperimentService, RunService }
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio._
import zio.console.Console
import zio.internal.PlatformLive

import scala.concurrent.Future

object MLFlowClient {
  type MLFLowResult[+A] = IO[Throwable, A]
  type Fun[OUT]         = AllService => MLFLowResult[OUT]

  sealed trait AllService extends ExperimentService with RunService with Console.Live {
    implicit def be: InternalClient
  }

  implicit class Executor[A](program: MLFLowResult[A]) {
    val runtime = Runtime(program, PlatformLive.Default)

    def result: Either[Throwable, A]              = runtime.unsafeRun(program.either)
    def asyncResult: Future[Either[Throwable, A]] = runtime.unsafeRunToFuture(program.either).future
  }

  def apply[OUT](mlflowURL: String)(f: Fun[OUT]): MLFLowResult[OUT] =
    for {
      client <- AsyncHttpClientZioBackend()
      env = new AllService {
        implicit val be: InternalClient = client

        def experimentService = new ExperimentServiceImpl(mlflowURL)
        def runService        = new RunServiceImpl(mlflowURL)
      }
      action <- ZIO.accessM[AllService] { services =>
        f(services).ensuring(client.close().ignore)
      } provide env
    } yield action

}
