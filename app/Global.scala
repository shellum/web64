import com.google.inject.{Guice, AbstractModule}
import play.api.GlobalSettings
import services.{Db, Redis}

object Global extends GlobalSettings {

  val injector = Guice.createInjector(new AbstractModule {
    protected def configure() {
      bind(classOf[Db]).to(classOf[Redis])
    }
  })

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    injector.getInstance(controllerClass)
  }
}