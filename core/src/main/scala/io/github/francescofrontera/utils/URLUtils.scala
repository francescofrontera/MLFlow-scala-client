package io.github.francescofrontera.utils

import java.net.URI

import sttp.model.Uri

object URLUtils {
  private[this] def composedURL(paths: Seq[String]): String => String =
    basePath => paths.foldLeft(basePath)((acc, path) => Seq(acc, path) mkString "/")

  def makeURL(pathParameters: Seq[String], basePath: String, queryParameters: Map[String, String] = Map.empty): Uri = {
    val normalizeQueryParameters = queryParameters map {
      case (key, value) => s"$key=$value"
    } mkString ","

    Uri(new URI(s"${composedURL(pathParameters)(basePath)}?$normalizeQueryParameters"))
  }
}
