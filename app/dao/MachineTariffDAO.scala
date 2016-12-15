package dao

import java.sql.Connection

import entity.MachineTariff
import play.api.Logger

/**
 * Created by wisnuwardoyo on 12/14/16.
 */
class MachineTariffDAO(connection: Connection) {

  def getMachineTariffByMachineId(id: Int): MachineTariff = {
    try {
      val statement = connection.prepareStatement("select * FROM `machine_tariff` JOIN `machine` ON `machine_tariff`.`machinetariff_machineid` = `machine`.`machineId` JOIN `tariff` ON `machine_tariff`.`machinetariff_tariffid` = `tariff`.`tariff_id` WHERE `machine`.`machineId` = ?")
      statement.setInt(1, id)

      val rs = statement.executeQuery()
      if (rs.next()) {
        val machineTariff: MachineTariff = new MachineTariff

        machineTariff.machineId = rs.getInt("machineId")
        machineTariff.machineName = rs.getString("machineName")
        machineTariff.machineStatus = rs.getInt("machineStatus")
        machineTariff.tariffId = rs.getInt("tariff_id")
        machineTariff.tariffName = rs.getString("tariff_name")
        machineTariff.tariffAmount = rs.getInt("tariff_amount")

        return machineTariff
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
