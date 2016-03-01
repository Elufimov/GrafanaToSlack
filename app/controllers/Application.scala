package controllers

import java.sql.Timestamp
import java.util.UUID
import javax.inject._

import dao.ImageDAO
import models.ImageModel
import play.api.Logger
import play.api.mvc._
import services.{ GrafanaService, SlackService }
import utils.Config

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success, Try }

@Singleton
class Application @Inject() (
    grafana: GrafanaService,
    imageDAO: ImageDAO,
    slack: SlackService
) extends Controller {

  def insert(panelId: Int, slug: String, from: String, to: String, width: Int, height: Int) = Action.async(parse.anyContent) { request ⇒
    grafana.downloadImage(panelId, slug, from, to, width, height).flatMap { downloadedFile ⇒
      imageDAO.insert(
        ImageModel(
          uuid = None,
          array = downloadedFile,
          timestamp = new Timestamp(new java.util.Date().getTime())
        )
      ).map { uuid ⇒
          request.body.asJson match {
            case Some(json) ⇒ Try((json \ "to").as[Seq[String]]) match {
              case Success(s) ⇒
                s.foreach { i ⇒
                  slack.send(i, s"${Config.url}:${Config.port}/image/${uuid.toString}").map { response ⇒
                    Logger.info(s"Slack push to: $i url: ${Config.url}:${Config.port}/image/${uuid.toString} status: ${response.body}")
                  }
                }
                Ok(uuid.toString)
              case Failure(e) ⇒
                Logger.error(e.toString)
                BadRequest(e.toString)
            }
            case None ⇒ Ok(uuid.toString)
          }
        }
    }
  }

  def image(uuid: UUID) = Action.async { request ⇒
    imageDAO.getByUUID(uuid).map { image ⇒
      Ok(image).as("image/jpg")
    }
  }

}