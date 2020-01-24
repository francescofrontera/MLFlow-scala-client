package io.github.francescofrontera.utils

import java.net.URI

object URLUtils {
  private[this] def composedURL(paths: Seq[String]): String => String =
    basePath => paths.foldLeft(basePath)((acc, path) => Seq(acc, path) mkString "/")

  def callGet(pathParameters: Seq[String], basePath: String, queryParameters: Map[String, String] = Map.empty): URI = {
    val normalizeQueryParameters = queryParameters map {
      case (key, value) => s"$key=$value"
    } mkString ","

    new URI(s"${composedURL(pathParameters)(basePath)}?$normalizeQueryParameters")
  }
}
