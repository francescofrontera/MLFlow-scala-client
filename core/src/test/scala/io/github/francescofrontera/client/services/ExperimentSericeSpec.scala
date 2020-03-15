package io.github.francescofrontera.client.services

import io.circe.parser._
import io.circe.{ Decoder, Encoder, Json }
import io.github.francescofrontera.client.internal.InternalClient
import io.github.francescofrontera.models.Experiment.ExperimentObject
import io.github.francescofrontera.models.Experiments
import sttp.model.Uri
import zio.test.Assertion._
import zio.test._
import zio.{ RIO, Task }

object Stubs {
  def apply(returnString: String): InternalClient =
    new InternalClient {
      def internalClient: InternalClient.Service[Any] =
        new InternalClient.Service[Any] {
          final def genericPost[E: Encoder, D: Decoder](uri: Uri, data: E): Task[D] = ???
          final def url: RIO[Any, String]                                           = RIO.succeed("")
          final def genericGet[D: Decoder](uri: Uri): Task[D] =
            RIO.fromTry(parse(returnString).getOrElse(Json.Null).as[D].toTry)
        }
    }

  final def getAllExperiment: InternalClient = this.apply(
    """
      |{
      |  "experiments": [
      |    {
      |      "experiment_id": "0",
      |      "name": "Default",
      |      "artifact_location": "file:///Users/francescofrontera/PycharmProjects/example/mlruns/0",
      |      "lifecycle_stage": "active"
      |    }
      |  ]
      |}
      |""".stripMargin
  )
}

object ExperimentSericeSpec
    extends DefaultRunnableSpec(
      suite("ExperimentServiceSpec")(
        testM("get all experiments") {
          val env = ExperimentService.Live

          val result = env.experimentService.getAll.provide(Stubs.getAllExperiment)

          assertM(result)(equalTo(
              Experiments(
                List(
                  ExperimentObject(
                    "Default",
                    Some(0),
                    Some("file:///Users/francescofrontera/PycharmProjects/example/mlruns/0"),
                    Some("active")
                  )
                )
              )
            ))
        }
      )
    )
