package io.github.francescofrontera.client.internal

import io.circe.{Decoder, Encoder}
import io.github.francescofrontera.utils.URLUtils
import sttp.client._
import sttp.client.asynchttpclient.WebSocketHandler
import sttp.client.circe._
import zio._

private[client] object InternalClient {
  type InternalClient = Has[InternalClient.Service]

  type S = SttpBackend[Task, Nothing, WebSocketHandler]

  trait Service {
    protected def url: Task[String]

    def genericGet[D: Decoder](uri: Seq[String], qParams: Map[String, String] = Map.empty): Task[D]
    def genericPost[E: Encoder, D: Decoder](uri: Seq[String], data: E): Task[D]
  }

  def live(mlflowURL: String): ZLayer[Has[SttpBackend[Task, zio.stream.Stream[Throwable,Byte], WebSocketHandler]], Nothing, Has[Service]] =
    ZLayer.fromFunction(
      be =>
        new Service {
          implicit val bImplicitly = be.get

          protected val url: RIO[Any, String] = RIO(mlflowURL)

          final def genericGet[D: Decoder](uri: Seq[String], qParams: Map[String, String] = Map.empty): Task[D] =
            for {
              baseUrl <- url
              makeURI <- RIO(
                URLUtils.makeURL(
                  pathParameters = uri,
                  basePath = baseUrl,
                  queryParameters = qParams
                )
              )
              decodeResult   <- basicRequest.get(makeURI).response(asJson[D]).send()
              resultAsEither <- Task.fromEither(decodeResult.body)
            } yield resultAsEither

          final def genericPost[E: Encoder, D: Decoder](uri: Seq[String], data: E): Task[D] =
            for {
              baseUrl <- url
              makeURI <- RIO(
                URLUtils.makeURL(
                  pathParameters = uri,
                  basePath = baseUrl
                )
              )
              decodeResult <- basicRequest.post(makeURI).body(data).response(asJson[D]).send()
              result       <- Task.fromEither(decodeResult.body)
            } yield result
      }
    )
}
