package io.github.francescofrontera.models

sealed case class ClientConfigEx(message: String) extends RuntimeException(message)
sealed case class ClientConfig(mlflowURL: String)
