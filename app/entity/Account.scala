package entity

import play.api.libs.json._

/**
 * Created by wisnuwardoyo on 12/12/16.
 */
class Account {

  var accountId: Int = 0
  var accountUsername: String = ""
  var accountRealname: String = ""
  var accountAddress: String = ""
  var accountCountryCode: String = ""
  var accountPhonenumber: String = ""
  var accountBalance: Int = 0
  var accountStatus: Int = 0;

  private def createAccount(id: Int, user: String, realName: String, address: String,
                            countryCode: String, phoneNumber: String, balance: Int, status: Int): Account = {
    val account: Account = new Account

    account.accountId = id;
    account.accountUsername = user;
    account.accountRealname = realName;
    account.accountAddress = address;
    account.accountCountryCode = countryCode
    account.accountPhonenumber = phoneNumber
    account.accountBalance = balance
    account.accountStatus = status
    return account
  }

  def fromJson(json: JsValue): Account = createAccount(
    (json \ "accountId").as[Int],
    (json \ "accountUser").as[String],
    (json \ "accountRealname").as[String],
    (json \ "accountAddress").as[String],
    (json \ "accountCountrycode").as[String],
    (json \ "accountPhonenumber").as[String],
    (json \ "accountBalance").as[Int],
    (json \ "accountStatus").as[Int]
  )

  def toJson: JsValue = JsObject(Seq(
    "accountId" -> JsNumber(this.accountId),
    "accountUser" -> JsString(this.accountUsername),
    "accountRealname" -> JsString(this.accountRealname),
    "accountAddress" -> JsString(this.accountAddress),
    "accountCountrycode" -> JsString(this.accountCountryCode),
    "accountPhonenumber" -> JsString(this.accountPhonenumber),
    "accountBalance" -> JsNumber(this.accountBalance),
    "accountStatus" -> JsNumber(this.accountStatus)
  ))
}

