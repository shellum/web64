import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.{JsPath, Reads, Json}
import play.api.test.Helpers._
import play.api.test._
import services.Colors

@RunWith(classOf[JUnitRunner])
class ColorsSpec extends Specification {
  implicit val personFormat = Json.format[Colors]
  "Colors" should {
    "render its page" in new WithApplication {
      val home = route(FakeRequest(GET, "/color")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("Colors")
    }

    "encode a string" in new WithApplication {
      val home = route(FakeRequest(GET, "/colorGetAll")).get
      val content = contentAsJson(home)
      val m = Json.fromJson(content)
      content must beEqualTo("YXNkZg")
    }

  }

}