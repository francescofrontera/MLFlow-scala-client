package io.github.francescofrontera.models
import io.circe._, io.circe.generic.semiauto._

sealed case class ExperimentsError(message: String) extends RuntimeException(message)
sealed case class Experiments(experiments: List[Experiment])

object Experiments {
  implicit val experimentsEncoder: Encoder[Experiments] = deriveEncoder
  implicit val experimentsDecoder: Decoder[Experiments] = deriveDecoder
}
