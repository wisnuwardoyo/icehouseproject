package entity

import java.util.Date

/**
 * Created by wisnuwardoyo on 12/14/16.
 */
class Transaction {

    var transactionId: Int = 0
    var transactionAccountId: Int = 0
    var transactionTimestamp: Date = new Date()
    var transactionRoutesId: Int = 0
    var transactionDetail: String = ""
    var transactionStatus: Int = 1

}
