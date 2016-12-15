package dao

import java.sql.{Timestamp, Connection}

import entity.Transaction
import play.api.Logger

/**
 * Created by wisnuwardoyo on 12/14/16.
 */
class TransactionDAO(connection: Connection) {

  def saveTranscation(transaction: Transaction): Boolean = {
    try{
      val statement = connection.prepareStatement("INSERT INTO `transaction` " +
        "(`transaction_routesid`, `transaction_accountid`, `transaction_timestamp`, `transaction_detail`) VALUES (?,?,?,?)")

      statement.setInt(1, transaction.transactionRoutesId)
      statement.setInt(2, transaction.transactionAccountId)
      statement.setTimestamp(3, new Timestamp(transaction.transactionTimestamp.getTime))
      statement.setString(4, transaction.transactionDetail)

      var result = statement.executeUpdate()
      return true
    }catch {
      case ex: Exception => {
        Logger.error(ex.getMessage)
        return false
      }
    }finally {
      connection.close()
    }
  }

}
