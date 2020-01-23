package io.github.francescofrontera.services.utils

import java.net.URI

import sttp.client._
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.model.Uri
import zio.Task

private[services] object Dispatch {
  private[this] def composedURL(paths: Seq[String]): String => String =
    basePath => paths.foldLeft(basePath)((acc, path) => Seq(acc, path) mkString "/")

  def callGet(pathParameters: Seq[String],
              basePath: String,
              queryParameters: Map[String, String] = Map.empty): Task[String] = {
    val normalizeQueryParameters = queryParameters map {
      case (key, value) => s"$key=$value"
    } mkString ","

    val javaURI =
      new URI(s"${composedURL(pathParameters)(basePath)}?$normalizeQueryParameters")

    AsyncHttpClientZioBackend() flatMap { implicit cli =>
      basicRequest.get(Uri(javaURI)).send().flatMap { resp =>
        Task.fromEither(resp.body.left.map(errMsg => new RuntimeException(errMsg)))
      }
    }
  }
}
