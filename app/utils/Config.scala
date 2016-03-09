package utils

import better.files.File
import play.Play._
/**
 * Created by elufimov on 08/02/16.
 */
object Config {
  val homeDir = File(application().configuration().getString("application.home"))
  val grafanaHost = application().configuration().getString("esgs.grafanaHost")
  val grafanaToken = application().configuration().getString("esgs.grafanaToken")
  val slackToken = application().configuration().getString("esgs.slackToken")
  val publicUrl = application().configuration().getString("esgs.publicUrl")
}
