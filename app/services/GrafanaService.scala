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
  def downloadImage(panelId: Int, slug: String, from: String, to: String, width: Int, height: Int) = {
    val url = s"/render/dashboard-solo/db/$slug/?panelId=$panelId&width=$width&height=$height&from=$from&to=$to"
    WS.url(s"${Config.grafanaHost}$url")
      .withHeaders("Authorization" → s"Bearer ${Config.grafanaToken}")
      .get().map(_.bodyAsBytes)
  }
}
