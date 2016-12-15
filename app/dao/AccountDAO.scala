package dao

import java.sql.Connection

import entity.Account
import play.api.Logger

/**
 * Created by wisnuwardoyo on 12/12/16.
 */
class AccountDAO(connection : Connection) {

  def login(username: String, password: String): Account ={
    try{
      val statement = connection.prepareStatement("SELECT * FROM `account` WHERE `account_username` = ? and `account_password` = ?")
      statement.setString(1, username)
      statement.setString(2, password)

      val rs = statement.executeQuery()
      if (rs.next()) {
        var account: Account = new Account
        account.accountId = rs.getInt("account_id")
        account.accountUsername = rs.getString("account_username")
        account.accountRealname = rs.getString("account_realname")
        account.accountAddress = rs.getString("account_address")
        account.accountCountryCode = rs.getString("account_countrycode")
        account.accountPhonenumber = rs.getString("account_phonenumber")

        return account
      } else {
        return null
      }
    }finally {
      connection.close()
    }

  }

  def getAccount(accountId: Int): Account = {
    try {
      val statement = connection.prepareStatement("SELECT * FROM `account` WHERE `account_id` = ?")
      statement.setInt(1, accountId)

      val rs = statement.executeQuery()
      if (rs.next()) {
        var account: Account = new Account
        account.accountId = rs.getInt("account_id")
        account.accountUsername = rs.getString("account_username")
        account.accountRealname = rs.getString("account_realname")
        account.accountAddress = rs.getString("account_address")
        account.accountCountryCode = rs.getString("account_countrycode")
        account.accountPhonenumber = rs.getString("account_phonenumber")
        account.accountBalance = rs.getInt("account_balance")
        account.accountStatus = rs.getInt("account_status")
        return account
      } else {
        return null
      }
    }catch {
      case ex: Exception =>{
        Logger.error(ex.getMessage)
        return null
      }
    }finally {
      connection.close()
    }
  }

  /**
   * Connection not finally closed
   * @param account
   * @return
   */
  def saveCurrentBalance(account: Account): Boolean ={
    try{
      val statement = connection.prepareStatement("UPDATE `account` SET `account_balance` = ? WHERE `account_id` = ?")
      statement.setInt(1, account.accountBalance)
      statement.setInt(2, account.accountId)

      var result = statement.executeUpdate()

      return true
    }catch {
      case ex: Exception => {
        Logger.error(ex.getMessage)
        return false
      }
    }finally {
      // connection.close()
    }

  }
  

}
