package io.github.francescofrontera.client.internal

import io.circe.Decoder
import sttp.client.asynchttpclient.WebSocketHandler
import sttp.model.Uri
import zio.Task

private[client] object ClientCall {
  import sttp.client._
  import sttp.client.circe._

  def genericGet[D: Decoder](uri: Uri)(implicit be: SttpBackend[Task, Nothing, WebSocketHandler]): Task[D] =
    for {
      decoodeResult  <- basicRequest.get(uri).response(asJson[D]).send()
      resultAsEither <- Task.fromEither(decoodeResult.body)
    } yield resultAsEither
}
