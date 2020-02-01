package io.github.francescofrontera.client.services

import io.circe.parser._
import io.circe.{ Decoder, Json }
import io.github.francescofrontera.client.internal.InternalClient
import io.github.francescofrontera.models.Experiment.ExperimentObject
import io.github.francescofrontera.models.Experiments
import sttp.model.Uri
import zio.test.Assertion._
import zio.test._
import zio.{ RIO, Task }

object Stubs {
  val stubClient = new InternalClient {
    override def internalClient: InternalClient.Service[Any] = new InternalClient.Service[Any] {
      override def url: RIO[Any, String] = RIO("")

      override def genericGet[D: Decoder](uri: Uri): Task[D] =
        RIO(parse("""
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
                    |""".stripMargin).getOrElse(Json.Null).as[D].toOption.get)
    }
  }
}

object ExperimentSericeSpec
    extends DefaultRunnableSpec(
      suite("ExperimentSericeSpec")(
        testM("get all experiments") {
          val env = ExperimentService.Live

          val result = env.experimentService.getAll
            .provide(Stubs.stubClient)

          assertM(result,
                  equalTo(
                    Experiments(
                      List(
                        ExperimentObject(0,
                                         "Default",
                                         "file:///Users/francescofrontera/PycharmProjects/example/mlruns/0",
                                         "active")
                      )
                    )
                  ))
        }
      )
    )
