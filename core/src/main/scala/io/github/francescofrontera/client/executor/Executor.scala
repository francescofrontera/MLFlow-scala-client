package io.github.francescofrontera.client.executor

import io.github.francescofrontera.client.MLFlow.MLFlowService
import zio._

object Executor {
  private val runner = new DefaultRuntime {}

  implicit class ExecutorOps[+A](result: MLFlowService[A]) {
    def asEither: Either[Throwable, A] = runner.unsafeRun(result.fold(err => Left(err), s => Right(s)))
  }
}
