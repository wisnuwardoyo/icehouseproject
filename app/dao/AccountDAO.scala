package dao

import java.sql.Connection

import entity.Account

/**
 * Created by wisnuwardoyo on 12/12/16.
 */
class AccountDAO(connection : Connection) {



  def login(username: String, password: String): Account ={
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
  }

}
