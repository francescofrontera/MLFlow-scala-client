package io.github.francescofrontera.models.error

trait MLFlowClientError extends RuntimeException {
  def message: String
}
