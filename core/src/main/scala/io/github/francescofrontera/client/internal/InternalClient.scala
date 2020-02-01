package io.github.francescofrontera.client.internal

import io.circe.Decoder
import sttp.client.asynchttpclient.WebSocketHandler
import sttp.client.circe.asJson
import sttp.client.{ basicRequest, SttpBackend }
import sttp.model.Uri
import zio.{ RIO, Task }

trait InternalClient {
  def internalClient: InternalClient.Service[Any]
}

private[client] object InternalClient {
  type S = SttpBackend[Task, Nothing, WebSocketHandler]

  final case class MatClient(url: String, client: SttpBackend[Task, Nothing, WebSocketHandler]) {
    implicit val clientImplicit = client
  }

  trait Service[R] {
    def url: RIO[R, String]

    def genericGet[D: Decoder](uri: Uri): Task[D]
  }

  sealed case class Live(mlflowURL: String)(implicit be: SttpBackend[Task, Nothing, WebSocketHandler])
      extends InternalClient {
    def internalClient: Service[Any] = new Service[Any] {
      def url: RIO[Any, String] = RIO(mlflowURL)

      def genericGet[D: Decoder](uri: Uri): Task[D] =
        for {
          decodeResult   <- basicRequest.get(uri).response(asJson[D]).send()
          resultAsEither <- Task.fromEither(decodeResult.body)
        } yield resultAsEither

    }
  }
}
