package io.github.francescofrontera.client.runner

import io.github.francescofrontera.client.services.{ ExperimentService, RunService }
import zio.Runtime
import zio.internal.PlatformLive

trait MLFlowDefaultRunner {
  final val execute = Runtime(new ExperimentService.Live with RunService.Live {}, PlatformLive.Default)
}
