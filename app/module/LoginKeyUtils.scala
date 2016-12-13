package module

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.JsValue

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

}
