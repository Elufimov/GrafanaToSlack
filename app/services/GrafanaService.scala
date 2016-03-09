package services

import javax.inject._

import play.api.libs.ws._
import utils.Config

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by elufimov on 08/02/16.
 */

@Singleton
class GrafanaService @Inject() (ws: WSClient) {
  def downloadImage(url: String) = {
    ws.url(url)
      .withHeaders("Authorization" → s"Bearer ${Config.grafanaToken}")
      .get().map(_.bodyAsBytes.toArray)
  }
}
