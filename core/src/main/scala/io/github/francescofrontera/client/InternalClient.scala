package io.github.francescofrontera.client

import sttp.client.SttpBackend
import sttp.client.asynchttpclient.WebSocketHandler
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.Task

private[client] trait InternalClient {
  def sttp: InternalClient.Serve
}

private[client] object InternalClient {
  type ClientType = SttpBackend[Task, Nothing, WebSocketHandler]

  trait Serve {
    def taskClient: Task[ClientType]
  }

  case object SttpMaterialization extends InternalClient.Serve {
    override def taskClient: Task[ClientType] = AsyncHttpClientZioBackend()
  }
}
