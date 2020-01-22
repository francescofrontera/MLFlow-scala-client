package io.github.francescofrontera.models

import io.circe._, io.circe.generic.semiauto._

sealed case class Experiment(experiment_id: Long, name: String, artifact_location: String, lifecycle_stage: String)

object Experiment {
  implicit val experimentEncoder: Encoder[Experiment] = deriveEncoder
  implicit val experimentDecoder: Decoder[Experiment] = deriveDecoder
}
