package models.session


import javax.inject.Singleton

import collection.JavaConversions._

/**
 * Created by wisnuwardoyo on 12/13/16.
 */
@Singleton
class DefaultSessionHandler extends  SessionHandler{

  val SESSIONS: java.util.HashMap[String, String] = new java.util.HashMap[String, String]()

  override def checkSession(s: (String, String)): Boolean = {
    this.synchronized{
      for(set: java.util.Map.Entry[String, String] <- SESSIONS.entrySet()){
        println(set +" "+ s)
        if(set.getKey.equals(s._1)){
          if(set.getValue.equals(s._2)){
            return true
          }
        }
      }
    }
    return false
  }

  override def addSession(s: (String, String)): Boolean = {
    this.synchronized{
      SESSIONS.put(s._1,s._2)
    }
    return true
  }

  override def clearAllSession(): Boolean = {
    SESSIONS.clear()
    return true
  }

  override def removeSession(key: String): Boolean = {
    SESSIONS.remove(key)
    return true
  }


}
