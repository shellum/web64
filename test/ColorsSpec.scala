import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json._
import play.api.mvc.SimpleResult
import play.api.test.Helpers._
import play.api.test._
import services.{Redis, Colors}
import org.specs2.mock.Mockito
import org.specs2.mutable._

import scala.concurrent.Future

class ColorsSpec extends PlaySpecification with Mockito {

  class ColorScope {

  }

  implicit val personFormat = Json.format[Colors]
  "Colors" should {
    "render its page" in new WithApplication {
      val home = route(FakeRequest(GET, "/color")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("Colors")
    }

    "encode a string" in  {
      val redisMock = mock[Redis]
      redisMock.getAllColors() returns JsObject(Seq(
        "startColor" -> JsString("a"),
        "endColor" -> JsString("b"),
        "key" -> JsString("c")
      ))
      val app = new controllers.Colors(redisMock)
      val result: Future[SimpleResult] = app.getAllColors()(FakeRequest())
      val json = contentAsJson(result)
      json.toString() must beEqualTo("""{"startColor":"a","endColor":"b","key":"c"}""")
    }

  }

}