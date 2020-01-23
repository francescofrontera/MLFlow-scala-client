package io.github.francescofrontera.services.utils

import io.circe.{ Decoder, Json }
import io.circe.parser._

import scala.util.Try

object EncodeDecode {
  def decode[A: Decoder](input: String): Try[A] =
    parse(input).getOrElse(Json.Null).as[A].toTry
}
