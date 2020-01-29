package io.github.francescofrontera.client

import io.github.francescofrontera.client.MLFlowClient.MLFLowResult
import zio.Runtime
import zio.internal.PlatformLive

import scala.concurrent.Future

object Executor {
  implicit class ExecutorOps[A](program: MLFLowResult[A]) {
    private[this] val runtime = Runtime(program, PlatformLive.Default)

    def result: Either[Throwable, A]              = runtime.unsafeRun(program.either)
    def asyncResult: Future[Either[Throwable, A]] = runtime.unsafeRunToFuture(program.either).future
  }
}
