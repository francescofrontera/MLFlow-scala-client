package io.github.francescofrontera.client.internal

import io.circe.{ Decoder, Encoder }
import sttp.client._
import sttp.client.asynchttpclient.WebSocketHandler
import sttp.client.circe._
import sttp.model.Uri
import zio._

private[client] object InternalClient {
  type InternalClient = Has[InternalClient.Service]

  type S = SttpBackend[Task, Nothing, WebSocketHandler]

  trait Service {
    def url: Task[String]

    def genericGet[D: Decoder](uri: Uri): Task[D]
    def genericPost[E: Encoder, D: Decoder](uri: Uri, data: E): Task[D]
  }

  def live(mlflowURL: String): ZLayer[Has[SttpBackend[Task, Nothing, WebSocketHandler]], Nothing, Has[Service]] =
    ZLayer.fromFunction(
      be =>
        new Service {
          implicit val bImplicitly            = be.get

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
    )
}
