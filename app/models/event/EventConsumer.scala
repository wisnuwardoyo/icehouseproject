package models.event

import play.api.libs.json.JsValue

/**
 * Created by wisnuwardoyo on 12/15/16.
 */
trait EventConsumer {

  def consumeEvent(): JsValue

}
