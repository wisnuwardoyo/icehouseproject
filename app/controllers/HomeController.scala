package controllers

import javax.inject._

import dao.AccountDAO
import models.session.SessionHandler
import module.{LoginKeyUtils, EncryptionModule}
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
class HomeController @Inject() (configuration: Configuration, session: SessionHandler) extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    session.checkSession("1" -> "{logkey}")
    Ok("")
    //Ok(views.html.index("Your new application is ready."))
  }

  /**
   * Renewing session
   * @return
   */
  def handshake = Action(parse.json) { request =>
    if (request == null) {
      Unauthorized(Json.obj("status" -> "Login Required", "message" -> ("http://" + request.host + "/login")))
    } else {
      try {
        var logkey = (request.body \ "logkey").asOpt[String].get
        val validation = LoginKeyUtils.isValid(logkey, configuration.getString("apps.maxAge").get.toLong * 60 * 1000)
        if (!validation._1) {
          BadRequest(Json.obj("status" -> "Session Expired", "message" -> ("http://" + request.host + "/login")))
        } else {
          val accountId = (validation._2 \ "accountId").asOpt[String].get
          if(session.checkSession(accountId -> logkey)){
            logkey = (LoginKeyUtils.getEncryptedKey(accountId))
            session.addSession(accountId -> logkey)
            Ok(Json.obj("status" -> "Handshake success", "logkey" -> logkey))
          }else{
            Unauthorized(Json.obj("status" -> "Handshake Failed", "message" -> (("http://" + request.host + "/login")+" Session no longer valid")))
          }
        }
      } catch {
        case ex: Exception => {
          BadRequest(Json.obj("status" -> "Handshake Failed", "message" -> ("http://" + request.host + "/login "+ex.toString)))
        }
      }
    }
  }

}
