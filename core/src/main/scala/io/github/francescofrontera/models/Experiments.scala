package io.github.francescofrontera.models
import io.circe._
import io.circe.generic.semiauto._
import io.github.francescofrontera.models.error.MLFlowClientError

sealed case class ExperimentsError(message: String) extends MLFlowClientError
sealed case class Experiments(experiments: List[Experiment.ExperimentObject])

object Experiments {
  implicit val experimentsEncoder: Encoder[Experiments] = deriveEncoder
  implicit val experimentsDecoder: Decoder[Experiments] = deriveDecoder
}
