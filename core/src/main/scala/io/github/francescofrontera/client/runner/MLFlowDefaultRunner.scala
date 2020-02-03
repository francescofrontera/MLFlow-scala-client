package io.github.francescofrontera.client.runner

import io.github.francescofrontera.client.MLFlowClient
import io.github.francescofrontera.client.services.{ ExperimentService, RunService }
import zio.Runtime
import zio.internal.PlatformLive

import scala.concurrent.Future

trait MLFlowDefaultRunner {
  final val execute = Runtime(
    r = new ExperimentService.Live with RunService.Live {},
    platform0 = PlatformLive.Default
  )

  implicit class Ops[OUT](in: MLFlowClient#ClientResult[OUT]) {
    final def result: OUT                 = execute.unsafeRun(in)
    final def resultAsFuture: Future[OUT] = execute.unsafeRunToFuture(in)
  }
}
