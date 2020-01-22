package io.github.francescofrontera.services

import dispatch.Defaults._
import dispatch._
import io.circe.Json
import io.circe.parser._
import io.github.francescofrontera.models._
import zio._

trait ExperimentService {
  def experimentService: ExperimentService.Service
}

object ExperimentService {
  type ExperimentResult[+A] = Task[A]

  trait Service {
    def getAll: ExperimentResult[Experiments]
    def getByExperimentId(id: String): ExperimentResult[Experiment]
  }

  abstract class ExperimentServiceImpl(clientConf: ClientConfig) extends ExperimentService.Service {
    def getAll: ExperimentResult[Experiments] =
      for {
        responseAsStr <- Task.fromFuture(
          _ => Http.default(composedURL(ExperimentPath ++ Seq("list"))(clientConf.mlflowURL) OK as.String)
        )
        task <- Task.fromEither(
          parse(responseAsStr)
            .getOrElse(Json.Null)
            .as[Experiments]
            .fold(
              err => Left(ExperimentsError(err.message)),
              experiments => Right(experiments)
            )
        )
      } yield task

    def getByExperimentId(id: String): ExperimentResult[Experiment] =
      for {
        responseAsString <- Task.fromFuture(
          _ =>
            Http.default(
              composedURL(ExperimentPath ++ Seq("get"))(clientConf.mlflowURL)
                .addQueryParameter("experiment_id", id) OK as.String
          )
        )
        task <- Task.fromEither(
          parse(responseAsString)
            .getOrElse(Json.Null)
            .hcursor
            .downField("experiment")
            .as[Experiment]
            .fold(
              err => Left(ExperimentsError(err.message)),
              experiments => Right(experiments)
            )
        )
      } yield task
  }

  //Private utils
  private[this] val ExperimentPath = "experiments" +: Nil

  private[this] def composedURL(paths: Seq[String]) =
    (basePath: String) => url(paths.foldLeft(basePath)((acc, path) => Seq(acc, path) mkString "/"))

  //Constructor
  def apply(clientConfig: ClientConfig): ExperimentService.Service =
    new ExperimentService {
      override def experimentService: Service = new ExperimentServiceImpl(clientConfig) {}
    }.experimentService
}
