package models.event

import java.util.concurrent.LinkedBlockingQueue

import play.api.libs.json.JsValue

/**
 * Created by wisnuwardoyo on 12/15/16.
 */
trait EventProducer {

  def produceEvent(js: JsValue): Boolean

}
