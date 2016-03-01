package services

import javax.inject.Singleton

import models.SlackMessageSingle
import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.ws.WS
import utils.Config
/**
 * Created by elufimov on 08/02/16.
 */
@Singleton
class SlackService {
  def sendMessage(message: SlackMessageSingle) = {
    WS.url(Config.slackToken).post(Json.toJson(message))
  }
}
