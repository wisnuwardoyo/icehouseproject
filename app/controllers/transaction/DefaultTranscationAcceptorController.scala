package controllers.transaction

import javax.inject.Inject

import dao.{AccountDAO, TransactionRoutesDAO}
import models.session.SessionHandler
import module.LoginKeyUtils
import play.api.Configuration
import play.api.db.Database
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.mvc.{Results, Action, Controller}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}


/**
 * Created by wisnuwardoyo on 12/13/16.
 */
class DefaultTranscationAcceptorController @Inject()(implicit context: ExecutionContext, configuration: Configuration, session: SessionHandler, db: Database, ws: WSClient) extends Controller {

  /**
   * Default Framing,
   * {"trxtype":TrxType,"trxvariable":JsValue,"logkey":key}
   * @return
   */
  def trx = Action(parse.json) { request => {
    val trxType = (request.body \ "trxtype").asOpt[String].get
    val trxVariable = (request.body \ "trxvariable").asOpt[String].get
    var logkey = (request.body \ "logkey").asOpt[String].get

    val validation = LoginKeyUtils.isValid(logkey, configuration.getString("apps.maxAge").get.toLong * 60 * 1000)
    if (validation._1) {
      val accountId = (validation._2 \ "accountId").asOpt[String].get
      if (session.checkSession(accountId -> logkey)) {
        val route = new TransactionRoutesDAO(db.getConnection()).getRoutes(trxType.toInt);
        val account = new AccountDAO(db.getConnection()).getAccount(accountId.toInt);
        if (account != null) {
          if(account.accountStatus == 2){
            Forbidden(Json.obj("status" -> "Blacklisted", "message" -> "Your account has been bloked. Please contact call center for more information"))
          }else{
            if (route != null) {
              val trxRequest: WSRequest = ws.url(route.transcationRoutes).withHeaders("Accept" -> "application/json")
              val futureResponse: Future[WSResponse] = trxRequest.post(Json.obj("trxtype" -> trxType, "trxvariable" -> trxVariable, "account" -> account.toJson))
              var responseStatus = 0
              var returnValue: JsValue = null;
              val futureResult: Future[Any] = futureResponse.map {
                response => {
                  logkey = (LoginKeyUtils.getEncryptedKey(accountId))
                  session.addSession(accountId -> logkey)
                  responseStatus = response.status;
                  returnValue = Json.obj("trxstatus" -> response.json, "logkey" -> logkey)
                }
              }
              Await.result(futureResult, 5000 millis)
              responseStatus match {
                case OK =>
                  Ok(returnValue)
                case INTERNAL_SERVER_ERROR =>
                  InternalServerError(returnValue)
                case EXPECTATION_FAILED =>
                  ExpectationFailed(returnValue)
                case BAD_REQUEST =>
                  BadRequest(returnValue)
                case _ =>
                  InternalServerError("")
              }
            } else {
              BadRequest(Json.obj("status" -> "Bad Request", "message" -> "No routes found for this request"))
            }
          }
        } else {
          Unauthorized(Json.obj("status" -> "No Account Found", "message" -> "Check your account or re-login to continue"))
        }
      } else {
        Unauthorized(Json.obj("status" -> "Transaction Failed", "message" -> "Session expired, check your account or re-login to continue"))
      }
    } else {
      Unauthorized(Json.obj("status" -> "Transaction Failed", "message" -> "Invalid session"))
    }

  }
  }

}
