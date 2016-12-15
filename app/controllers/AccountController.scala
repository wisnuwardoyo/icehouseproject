package controllers

import com.google.inject.Inject
import dao.AccountDAO
import models.session.SessionHandler
import module.LoginKeyUtils
import play.api.Configuration
import play.api.db.Database
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

/**
 * Created by wisnuwardoyo on 12/15/16.
 */
class AccountController @Inject()(db: Database, configuration: Configuration, session: SessionHandler) extends Controller {

  def getAccount = Action(parse.json) { request => {
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
            val account = new AccountDAO(db.getConnection()).getAccount(accountId.toInt);
            Ok(Json.obj("status" -> "Get Profile success", "account" -> account.toJson))
          }else{
            Unauthorized(Json.obj("status" -> "Get Profile Failed", "message" -> (("http://" + request.host + "/login")+" Session no longer valid")))
          }
        }
      } catch {
        case ex: Exception => {
          BadRequest(Json.obj("status" -> "Get Profile Failed", "message" -> ("http://" + request.host + "/login "+ex.toString)))
        }
      }
    }
  }
  }


}
