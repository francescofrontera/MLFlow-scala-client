package io.github.francescofrontera.models.error

abstract class MLFlowClientError(message: String) extends RuntimeException(message)
