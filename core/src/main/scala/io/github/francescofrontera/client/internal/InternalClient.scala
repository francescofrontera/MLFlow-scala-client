package io.github.francescofrontera.client.internal

import io.circe.{ Decoder, Encoder }
import sttp.client._
import sttp.client.asynchttpclient.WebSocketHandler
import sttp.client.circe._
import sttp.model.Uri
import zio._

trait InternalClient {
  def internalClient: InternalClient.Service[Any]
}

private[client] object InternalClient {
  type S = SttpBackend[Task, Nothing, WebSocketHandler]

  trait Service[R] {
    def url: RIO[R, String]

    def genericGet[D: Decoder](uri: Uri): Task[D]
    def genericPost[E: Encoder, D: Decoder](uri: Uri, data: E): Task[D]
  }

  sealed case class Live(mlflowURL: String)(implicit be: S) extends InternalClient {
    def internalClient: Service[Any] = new Service[Any] {
      def url: RIO[Any, String] = RIO(mlflowURL)

      final def genericGet[D: Decoder](uri: Uri): Task[D] =
        for {
          decodeResult   <- basicRequest.get(uri).response(asJson[D]).send()
          resultAsEither <- Task.fromEither(decodeResult.body)
        } yield resultAsEither

      final def genericPost[E: Encoder, D: Decoder](uri: Uri, data: E): Task[D] =
        for {
          decodeResult <- basicRequest.post(uri).body(data).response(asJson[D]).send()
          result       <- Task.fromEither(decodeResult.body)
        } yield result
    }
  }
}
