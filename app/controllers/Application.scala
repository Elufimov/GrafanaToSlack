package controllers

import java.sql.Timestamp
import java.util.UUID
import javax.inject._

import dao.ImageDAO
import models._
import play.api.mvc._
import services.{ GrafanaService, SlackService }
import utils.Config

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class Application @Inject() (
    grafana: GrafanaService,
    imageDAO: ImageDAO,
    slack: SlackService
) extends Controller {
  def forward() = Action.async(parse.json[SlackMessageMultiple]) { request ⇒
    request.body.attachments match {
      case None ⇒
        Future {
          request.body.channels.foreach { channel ⇒
            slack.sendMessage(
              SlackMessageSingle(
                username = request.body.username,
                icon_emoji = request.body.icon_emoji,
                text = request.body.text,
                channel = channel,
                attachments = None
              )
            )
          }
          Ok("")
        }
      case Some(e) ⇒
        val listOfFuture = e.map { attach ⇒
          if (attach.image_url.exists(_.startsWith(Config.grafanaHost))) {
            grafana.downloadImage(attach.image_url.get).flatMap { downloadedFile ⇒
              imageDAO.insert(
                ImageModel(
                  uuid = None,
                  array = downloadedFile,
                  timestamp = new Timestamp(new java.util.Date().getTime())
                )
              )
            }.map { uuid ⇒
              attach.copy(image_url = Some(s"${Config.url}:${Config.port}/image/${uuid.toString}"))
            }
          } else {
            Future {
              attach
            }
          }
        }
        Future.sequence(listOfFuture).map { list ⇒
          request.body.channels.foreach { channel ⇒
            slack.sendMessage(
              SlackMessageSingle(
                username = request.body.username,
                icon_emoji = request.body.icon_emoji,
                text = request.body.text,
                channel = channel,
                attachments = Some(list)
              )
            )
          }
          Ok("")
        }
    }
  }

  def image(uuid: UUID) = Action.async { request ⇒
    imageDAO.getByUUID(uuid).map { image ⇒
      Ok(image).as("image/jpg")
    }
  }

}