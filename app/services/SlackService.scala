package services

import javax.inject.Singleton

import net.gpedro.integrations.slack.{ SlackApi, SlackAttachment, SlackMessage }
import utils.Config

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
 * Created by elufimov on 08/02/16.
 */
@Singleton
class SlackService {
  def client = new SlackApi(Config.slackToken)
  def sendMessage(to: String, imageUrl: String) = {
    Future {
      val msg = new SlackMessage(to, Config.slackBotName, "")
      val attach = new SlackAttachment()
        .setFallback(imageUrl)
        .setImageUrl(imageUrl)
      msg.addAttachments(attach)
      client.call(msg)
    }
  }

}
