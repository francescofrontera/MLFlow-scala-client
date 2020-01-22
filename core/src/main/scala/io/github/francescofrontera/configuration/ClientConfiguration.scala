package io.github.francescofrontera.configuration

import io.github.francescofrontera.models.{ ClientConfig, ClientConfigEx }
import pureconfig._, pureconfig.generic.auto._
import zio.{ Task, ZIO }

trait ClientConfiguration {
  def config: ClientConfiguration.Service
}

object ClientConfiguration {
  trait Service {
    def materializeConfig: Task[ClientConfig]
  }

  class AppConf extends ClientConfiguration.Service {
    override def materializeConfig: Task[ClientConfig] =
      ZIO
        .fromEither(ConfigSource.default.load[ClientConfig])
        .mapError[ClientConfigEx](err => ClientConfigEx(err.toList.mkString))
  }
}
