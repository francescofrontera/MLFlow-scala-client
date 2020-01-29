package io.github.francescofrontera

import io.circe.Decoder
import sttp.client.SttpBackend
import sttp.client.asynchttpclient.WebSocketHandler
import sttp.model.Uri
import zio.Task

package object client {
  type InternalClient = SttpBackend[Task, Nothing, WebSocketHandler]

  //TODO: use Module Patter here (for better test) ???.
  object ClientCall {
    import sttp.client._
    import sttp.client.circe._

    def genericGet[D: Decoder](uri: Uri)(implicit be: InternalClient): Task[D] =
      for {
        dResult        <- basicRequest.get(uri).response(asJson[D]).send()
        resultAsEither <- Task.fromEither(dResult.body)
      } yield resultAsEither
  }
}
