package controllers.transaction.app_highway

import java.util.Date

import com.google.inject.Inject
import dao.{AccountDAO, MachineTariffDAO, TransactionDAO}
import entity.{Account, MachineTariff, Transaction}
import models.event.EventProducer
import module.EncryptionModule
import play.api.db.Database
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller}

/**
 * Created by wisnuwardoyo on 12/13/16.
 */
class HighwayTransactionController @Inject()(db: Database, event: EventProducer) extends Controller {

  /**
   * WS for Highway transaction
   * Default Framing
   * {"trxtype":"$trxtype","trxvariable":{"machineid":"$id"}, "account":"{}"}
   * if success auto publish event
   * @return
   */
  def debit = Action(parse.json) { r => {
    val trxType = (r.body \ "trxtype").asOpt[String].get
    val trxVariable = Json.parse((r.body \ "trxvariable").asOpt[String].get);
    val account = new Account().fromJson((r.body \ "account").get)

    val machineTariff = new MachineTariffDAO(db.getConnection()).getMachineTariffByMachineId((trxVariable \ "machineid").asOpt[String].get.toInt)
    if (machineTariff != null) {
      if (account.accountBalance >= machineTariff.tariffAmount) {

        //TODO : this should inside model
        val trxTimestamp: Date = new Date()
        val recipe = generateDebitReceipt(account, machineTariff, trxTimestamp.getTime)
        val trx: Transaction = new Transaction

        trx.transactionAccountId = account.accountId
        trx.transactionRoutesId = trxType.toInt
        trx.transactionTimestamp = trxTimestamp
        trx.transactionDetail = recipe.toString()
        trx.transactionStatus = 0;

        account.accountBalance = account.accountBalance - machineTariff.tariffAmount;
        val connection = db.getConnection(false);
        if (new AccountDAO(connection).saveCurrentBalance(account) && new TransactionDAO(db.getConnection()).saveTranscation(trx)) {
          connection.commit()
          connection.close()

          event.produceEvent(Json.obj("machineid" -> machineTariff.machineId, "status" -> "Transaction Success", "detail" -> recipe))
          Ok(Json.obj("status" -> "Transaction Success", "detail" -> recipe))
        } else {
          connection.close()
          InternalServerError(Json.obj("status" -> "Internal Server Error", "Message" -> "An error occurred when trying to save transaction"))
        }
      } else {
        ExpectationFailed(Json.obj("status" -> "Insufficient Balance", "Message" -> "Please top up your account"))
      }
    } else {
      BadRequest(Json.obj("status" -> "Invalid Request", "message" -> "No machine \\ tariff found"))
    }
  }
  }


  private def generateDebitReceipt(account: Account, machineTariff: MachineTariff, timestamp: Long): JsValue = {
    val signature = EncryptionModule.encrypt(account.accountId + "," + timestamp + "," + account.accountBalance + "," + (account.accountBalance - machineTariff.tariffAmount));
    return Json.obj(
      "trxTimestamp" -> timestamp,
      "balancebefore" -> account.accountBalance,
      "balanceafter" -> (account.accountBalance - machineTariff.tariffAmount),
      "amount" -> (machineTariff.tariffAmount),
      "machineid" -> machineTariff.machineId,
      "tarifid" -> machineTariff.tariffId,
      "signature" -> signature
    )
  }


}
