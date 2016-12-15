package models.event

import java.util.concurrent.LinkedBlockingQueue
import javax.inject.Singleton

import play.api.libs.json.JsValue

/**
 * Created by wisnuwardoyo on 12/15/16.
 */
@Singleton
class DefaultEventHandler extends EventProducer with EventConsumer{

  val QUEUE = new LinkedBlockingQueue[JsValue]()

  override def produceEvent(js: JsValue): Boolean = {
    QUEUE.put(js)
    return true;
  }

  /**
   * Warning null pointer exception
   * @return JsValue if exist, null if not exist
   */
  override def consumeEvent(): JsValue = {
      return QUEUE.poll()
  }
}
