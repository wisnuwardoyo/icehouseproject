package controllers

import javax.inject._

import module.EncryptionModule
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.Configuration
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (configuration: Configuration) extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    val s = "{\"logintime\":\"2016-12-12 15:05:00\"}"
    Ok(EncryptionModule.encrypt(s))
    //Ok(views.html.index("Your new application is ready."))
  }

  def handshake = Action(parse.json) { request =>
    if (request == null) {
      Unauthorized(Json.obj("Status" -> "Login Required", "message" -> ("http://" + request.host + "/login")))
    } else {
      try {
        val json: JsValue = Json.parse(EncryptionModule.decrypt((request.body \ "logkey").asOpt[String].get))
        val loginTime = (json \ "logintime").asOpt[String].get
        val expiredTime = configuration.getString("apps.maxAge").get.toLong * 60 * 1000
        val c: DateTime = new DateTime(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(loginTime))

        if (new DateTime().getMillis > c.getMillis + expiredTime) {
          BadRequest(Json.obj("Status" -> "Session Expired", "message" -> ("http://" + request.host + "/login")))
        } else {
          Ok(c.toDate.getTime.toString + " " + expiredTime)
        }
      } catch {
        case ex: Exception => {
          BadRequest(Json.obj("Status" -> "Handshake Failed", "message" -> ("http://" + request.host + "/login "+ex.toString)))
        }
      }
    }
  }

}
