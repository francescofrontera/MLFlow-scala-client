package io.github.francescofrontera.client.services

import io.circe.parser._
import io.circe.{ Decoder, Encoder, Json }
import io.github.francescofrontera.client.internal.InternalClient
import io.github.francescofrontera.models.Experiment.ExperimentObject
import io.github.francescofrontera.models.Experiments
import sttp.model.Uri
import zio.test.Assertion._
import zio.test._
import zio._

object Stubs {
  private[this] def stubber(returnString: String): Layer[Nothing, Has[InternalClient.Service]] =
    ZLayer.succeed(new InternalClient.Service {
      def genericPost[E: Encoder, D: Decoder](uri: Uri, data: E): Task[D] =
        Task.fail(throw new RuntimeException("No Post call yet"))
      def url: RIO[Any, String] = RIO.succeed("file:///./")
      def genericGet[D: Decoder](uri: Uri): Task[D] =
        RIO.fromTry(parse(returnString).getOrElse(Json.Null).as[D].toTry)
    })

  final def allExperiments: ZIO[Any, Throwable, Experiments] = {
    val layer = this.stubber(
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

    getAllExperimentService.provideLayer(layer >>> ExperimentService.live)
  }
}

object ExperimentSericeSpec extends DefaultRunnableSpec {
  def spec = suite("ExperimentServiceSpec")(
    testM("get all experiments") {
      assertM(Stubs.allExperiments)(
        equalTo(
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
        )
      )
    }
  )

}
