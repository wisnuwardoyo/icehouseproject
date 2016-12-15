package dao

import java.sql.Connection

import entity.TransactionRoutes
import play.api.Logger

/**
 * Created by wisnuwardoyo on 12/13/16.
 */
class TransactionRoutesDAO(connection: Connection) {

  def getRoutes(id: Int): TransactionRoutes = {
    try {
      val statement = connection.prepareStatement("SELECT * FROM `transaction_routes` where `transactionroutes_id` = ?")
      statement.setInt(1, id)

      val rs = statement.executeQuery()
      if (rs.next()) {
        val routes: TransactionRoutes = new TransactionRoutes(rs.getInt("transactionroutes_id"), rs.getString("transactionroutes_routes"), rs.getString("transactionroutes_description"))
        return routes;
      } else {
        return null
      }
    } catch {
      case ex: Exception =>
        Logger.error(ex.getMessage)
        return null
    }
    finally {
      connection.close()
    }
  }

}
