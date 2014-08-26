import com.google.inject.name.Names
import com.google.inject.{TypeLiteral, AbstractModule, Guice}
import play.api.GlobalSettings
import redis.clients.jedis.Jedis
import services.{Db, Redis}

object Global extends GlobalSettings {

  val injector = Guice.createInjector(new AbstractModule {
    protected def configure() {
      bind(classOf[Db]).to(classOf[Redis])
      bindConstant().annotatedWith(Names.named("Redis Host")).to("localhost")
    }
  })

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    injector.getInstance(controllerClass)
  }
}