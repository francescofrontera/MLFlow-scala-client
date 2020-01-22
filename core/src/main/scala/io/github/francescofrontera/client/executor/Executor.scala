package io.github.francescofrontera.client.executor

import io.github.francescofrontera.client.MLFlow.MLFlowResult
import zio._

object Executor {
  private val runner = new DefaultRuntime {}

  implicit class ExecutorOps[+A](result: MLFlowResult[A]) {
    def asEither: Either[Throwable, A] = runner.unsafeRun(result.either)
  }
}
