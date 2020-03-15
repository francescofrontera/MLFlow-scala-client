package io.github.francescofrontera.models
import io.circe._
import io.circe.generic.semiauto._
import io.github.francescofrontera.models.Experiment.ExperimentObject
import io.github.francescofrontera.models.error.MLFlowClientError

final case class ExperimentsError(message: String) extends MLFlowClientError(message)

final case class Experiments(experiments: List[ExperimentObject])

object Experiments {
  implicit val experimentsEncoder: Encoder[Experiments] = deriveEncoder
  implicit val experimentsDecoder: Decoder[Experiments] = deriveDecoder
}
