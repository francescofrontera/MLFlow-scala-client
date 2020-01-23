package io.github.francescofrontera.models

import io.circe._
import io.circe.generic.semiauto._

final case class Experiment(experiment: Experiment.ExperimentObject)

object Experiment {
  final case class ExperimentObject(
      experiment_id: Long,
      name: String,
      artifact_location: String,
      lifecycle_stage: String
  )

  implicit lazy val experimentObjectEncoder: Encoder[ExperimentObject] = deriveEncoder
  implicit lazy val experimentObjectDecoder: Decoder[ExperimentObject] = deriveDecoder

  implicit lazy val experimentEncoder: Encoder[Experiment] = deriveEncoder
  implicit lazy val experimentDecoder: Decoder[Experiment] = deriveDecoder

}
