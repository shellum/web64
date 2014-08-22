import org.specs2.execute.Results
import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import play.api.libs.json._
import play.api.mvc.SimpleResult
import play.api.test._
import services.{Colors, Redis}

import scala.concurrent.Future

class ColorsSpec extends PlaySpecification with Results with Mockito {

  implicit val personFormat = Json.format[Colors]

  class ColorScope extends Scope {
    val redisMock = mock[Redis]
    redisMock.getAllColors() returns JsObject(Seq(
      "startColor" -> JsString("a"),
      "endColor" -> JsString("b"),
      "key" -> JsString("c")
    ))
    val colorsString = """{"startColor":"a","endColor":"b","key":"c"}"""
    val app = new controllers.Colors(redisMock)
  }
  "Colors" should {
    "render its page" in new WithApplication {
      val home = route(FakeRequest(GET, "/color")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("Colors")
    }

    "encode a string" in new ColorScope {
      val result: Future[SimpleResult] = app.getAllColors()(FakeRequest())
      val json = contentAsJson(result)
      json.toString() must beEqualTo(colorsString)
    }

  }

}