package io.github.francescofrontera.client.internal

import sttp.client.SttpBackend
import sttp.client.asynchttpclient.WebSocketHandler
import zio.{ RIO, Task }

trait InternalClient {
  def internalClient: InternalClient.Service[Any]
}

private[client] object InternalClient {
  final case class MatClient(url: String, client: SttpBackend[Task, Nothing, WebSocketHandler]) {
    implicit val clientImplicit = client
  }

  trait Service[R] {
    def getClient: RIO[R, MatClient]
  }

  sealed case class Live(mlflowURL: String, in: SttpBackend[Task, Nothing, WebSocketHandler]) extends InternalClient {
    def internalClient: Service[Any] = new Service[Any] {
      override def getClient: RIO[Any, MatClient] = RIO.succeed(MatClient(mlflowURL, in))
    }
  }

}
