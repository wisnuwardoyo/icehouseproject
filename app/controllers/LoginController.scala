package controllers


import javax.inject.Inject

import dao.AccountDAO
import entity.Account
import models.session.SessionHandler
import module.{LoginKeyUtils, EncryptionModule}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.db._
import play.api.libs.json.Json
import play.api.mvc._

/**
 * Created by wisnuwardoyo on 12/12/16.
 */
class LoginController @Inject() (db: Database, session: SessionHandler) extends Controller{

    def login = Action(parse.json) {request =>
      try{
        val username = (request.body \ "username").asOpt[String].get
        val password = md5Hash((request.body \ "password").asOpt[String].get)

        val account: Account = new AccountDAO(db.getConnection(true)).login(username, password);
        if(account != null){
          val logkey = LoginKeyUtils.getEncryptedKey(account.accountId+"")
          session.addSession(account.accountId+"" -> logkey)
          Ok(Json.obj("status" -> "login successfull",
            "logkey" -> logkey
          , "account" -> account.toJson))
        }else{
          Unauthorized(Json.obj("status" -> "login failed", "message" -> "Check your username or password"))
        }
      }catch {
        case ex: Exception =>{
          InternalServerError(Json.obj("status" -> "login failed", "message" -> "Check your username or password"))
        }
      }
    }

  def md5Hash(text: String): String = java.security.MessageDigest.getInstance("MD5").digest(text.getBytes()).map(0xFF & _).map {
    "%02x".format(_)
  }.foldLeft("") {
    _ + _
  }

}
