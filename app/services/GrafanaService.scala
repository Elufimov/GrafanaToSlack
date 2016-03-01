package services

import javax.inject._

import play.api.Play.current
import play.api.libs.ws.WS
import utils.Config

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by elufimov on 08/02/16.
 */

@Singleton
class GrafanaService {
  def downloadImage(url: String) = {
    WS.url(url)
      .withHeaders("Authorization" → s"Bearer ${Config.grafanaToken}")
      .get().map(_.bodyAsBytes)
  }
}
