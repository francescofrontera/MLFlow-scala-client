package io.github.francescofrontera.models

import io.circe._, io.circe.generic.semiauto._

final case class Run(run: Run.RunObject)

object Run {
  final case class Info(run_uuid: String,
                        experiment_id: String,
                        user_id: String,
                        status: String,
                        start_time: String,
                        end_time: String,
                        artifact_uri: String,
                        lifecycle_stage: String,
                        run_id: String)

  final case class Metric(key: String, value: Double, timestamp: String, step: String)
  final case class Param(key: String, value: String)
  final case class Tag(key: String, value: String)

  final case class Data(metrics: List[Metric], params: List[Param], tags: List[Tag])

  final case class RunObject(info: Run.Info, data: Run.Data)

  //FIXME: Consider to split all decoder and compose there....
  //Decoders
  lazy implicit val runInfoDecoder: Decoder[Info]        = deriveDecoder[Info]
  lazy implicit val runMetricDecoder: Decoder[Metric]    = deriveDecoder[Metric]
  lazy implicit val runParamDecoder: Decoder[Param]      = deriveDecoder[Param]
  lazy implicit val runTagDecoder: Decoder[Tag]          = deriveDecoder[Tag]
  lazy implicit val runDataDecoder: Decoder[Data]        = deriveDecoder[Data]
  lazy implicit val runObjectDecoder: Decoder[RunObject] = deriveDecoder[RunObject]

  lazy implicit val runDecoder: Decoder[Run] = deriveDecoder[Run]

  //Encoders
  lazy implicit val runInfoEncoder: Encoder[Info]        = deriveEncoder[Info]
  lazy implicit val runMetricEncoder: Encoder[Metric]    = deriveEncoder[Metric]
  lazy implicit val runParamEncoder: Encoder[Param]      = deriveEncoder[Param]
  lazy implicit val runTagEncoder: Encoder[Tag]          = deriveEncoder[Tag]
  lazy implicit val runDataEncoder: Encoder[Data]        = deriveEncoder[Data]
  lazy implicit val runObjectEncoder: Encoder[RunObject] = deriveEncoder[RunObject]

  lazy implicit val runEncoder: Encoder[Run] = deriveEncoder[Run]
}
