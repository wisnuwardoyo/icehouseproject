import com.google.inject.AbstractModule
import models.event.{EventConsumer, DefaultEventHandler, EventProducer}
import models.session.{DefaultSessionHandler, SessionHandler}

/**
 * Created by wisnuwardoyo on 12/13/16.
 */
class Module extends AbstractModule {
  override def configure() = {
    //Set DefaultSessionHandler as the implementation of SessionHandler
    bind(classOf[SessionHandler]).to(classOf[DefaultSessionHandler])
    bind(classOf[EventProducer]).to(classOf[DefaultEventHandler])
    bind(classOf[EventConsumer]).to(classOf[DefaultEventHandler])

  }
}
