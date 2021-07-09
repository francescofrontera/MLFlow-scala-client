package io.github.francescofrontera.client.services

import io.circe.parser._
import io.circe.{Decoder, Encoder, Json}
import io.github.francescofrontera.client.internal.InternalClient
import io.github.francescofrontera.models.Experiment.ExperimentObject
import io.github.francescofrontera.models.Experiments
import zio._
import zio.test.Assertion._
import zio.test._

object Stubs {
  private[this] def stub(returnString: String): Layer[Nothing, Has[InternalClient.Service]] =
    ZLayer.succeed(new InternalClient.Service {
      protected val url: Task[String] = RIO.succeed("file:///./")

      def genericGet[D: Decoder](uri: Seq[String], qParams: Map[String, String]): Task[D] =
        RIO.fromTry(parse(returnString).getOrElse(Json.Null).as[D].toTry)

      def genericPost[E: Encoder, D: Decoder](uri: Seq[String], data: E): Task[D] =
        Task.fail(throw new RuntimeException("..."))
    })

  final def allExperiments: ZIO[Any, Throwable, Experiments] = {
    val layer = this.stub(
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

object ExperimentServiceSpec extends DefaultRunnableSpec {
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
