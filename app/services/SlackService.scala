package services

import javax.inject.Singleton
import utils.Config
import play.api.Play.current
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
 * Created by elufimov on 08/02/16.
 */
@Singleton
class SlackService {
  def send(channel: String, imageUrl: String) = {
    WS.url(Config.slackToken).post(
      s"""
        |{
        | "username": "${Config.slackBotName}",
        | "channel": "$channel",
        | "attachments": [
        |   {
        |     "text": "$imageUrl",
        |     "image_url": "$imageUrl"
        |   }
        | ]
        |}
      """.stripMargin
    )
  }
}
