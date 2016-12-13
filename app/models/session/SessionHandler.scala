package models.session

/**
 * Created by wisnuwardoyo on 12/13/16.
 */
trait SessionHandler {

  def addSession(s: (String, String)) : Boolean

  def checkSession(s: (String, String)): Boolean

  def removeSession(key: String): Boolean

  def clearAllSession(): Boolean

}
