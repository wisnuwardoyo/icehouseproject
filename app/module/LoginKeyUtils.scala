package module

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{Json, JsValue}

/**
 * Created by wisnuwardoyo on 12/12/16.
 */
object LoginKeyUtils {


  def getDecrytedKey(logkey: String) : String ={
    return EncryptionModule.decrypt(logkey)
  }

  def getEncryptedKey(accountId: String): String ={
    return EncryptionModule.encrypt("{\"logintime\":\""+(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(new DateTime()))+"\", \"accountId\" : \""+accountId+"\"}")
  }

  def isValid(logkey: String, expiredTime: Long): (Boolean, JsValue) ={
    try{
      val json: JsValue = Json.parse(LoginKeyUtils.getDecrytedKey(logkey))
      val loginTime = (json \ "logintime").asOpt[String].get
      val c: DateTime = new DateTime(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(loginTime))
      if (new DateTime().getMillis > c.getMillis + expiredTime) {
          return false -> null;
      }else{
        return true -> json;
      }
    }catch {
      case ex: Exception =>{
          return false -> null;
      }
    }
    return false -> null;
  }


}
