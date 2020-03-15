package io.github.francescofrontera.models

import io.circe._
import io.circe.generic.semiauto._

final case class Experiment(experiment: Experiment.ExperimentObject)
final case class ExperimentResponse(experiment_id: String)

object Experiment {
  final case class ExperimentObject(
      name: String,
      experiment_id: Option[Long] = None,
      artifact_location: Option[String] = None,
      lifecycle_stage: Option[String] = None
  )

  implicit lazy val experimentObjectEncoder: Encoder[ExperimentObject] = deriveEncoder
  implicit lazy val experimentObjectDecoder: Decoder[ExperimentObject] = deriveDecoder

  implicit lazy val experimentEncoder: Encoder[Experiment] = deriveEncoder
  implicit lazy val experimentDecoder: Decoder[Experiment] = deriveDecoder

  //POST response
  implicit lazy val experimentResponseEncoder: Encoder[ExperimentResponse] = deriveEncoder
  implicit lazy val experimentResponseDecoder: Decoder[ExperimentResponse] = deriveDecoder

}
