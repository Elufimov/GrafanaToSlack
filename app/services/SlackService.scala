package services

import javax.inject.{ Inject, Singleton }

import models.SlackMessageSingle
import play.api.libs.json.Json
import play.api.libs.ws._
import utils.Config

/**
 * Created by elufimov on 08/02/16.
 */
@Singleton
class SlackService @Inject() (ws: WSClient) {
  def sendMessage(message: SlackMessageSingle) = {
    ws.url(Config.slackToken).post(Json.toJson(message))
  }
}
