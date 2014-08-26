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
    redisMock.getAllGradients() returns JsObject(Seq(
      "startColor" -> JsString("a"),
      "endColor" -> JsString("b"),
      "key" -> JsString("c")
    ))
    val colorsString = """{"startColor":"a","endColor":"b","key":"c"}"""
    val app = new controllers.Colors(redisMock)
  }
  "Colors" should {
    "render its page" in new ColorScope {
      val result: Future[SimpleResult] = app.index()(FakeRequest())
      status(result) must equalTo(OK)
      contentType(result) must beSome.which(_ == "text/html")
      contentAsString(result) must contain("Colors")
    }

    "test getAllGradients" in new ColorScope {
      val result: Future[SimpleResult] = app.getAllColors()(FakeRequest())
      val json = contentAsJson(result)
      json.toString() must beEqualTo(colorsString)
    }

    "test GradientSave" in new ColorScope {
      val result: Future[SimpleResult] = app.save()(FakeRequest().withFormUrlEncodedBody(("startColor","a"),("endColor","b")))
      status(result) must be equalTo (OK)
      there was one(redisMock).addGradient(any[String],any[String])
    }

    "get random hex colors" in new ColorScope {
      val hexColorHeader = '#'
      val hexColorLength = 7
      val colors = new controllers.Colors(redisMock)
      val color1 = colors.getRandomHexColor
      val color2 = colors.getRandomHexColor
      color1.charAt(0) must beEqualTo(hexColorHeader)
      color2.charAt(0) must beEqualTo(hexColorHeader)
      color1.length must beEqualTo(hexColorLength)
      color2.length must beEqualTo(hexColorLength)
      color1 must not equalTo(color2)
    }

    "remove a gradient" in new ColorScope {
      val result: Future[SimpleResult] = app.removeGradient("mykey")(FakeRequest())
      status(result) must be equalTo (OK)
      there was one(redisMock).delVals(any[String])
    }

    "get a random gradient" in new ColorScope {
      val result: Future[SimpleResult] = app.getRandomGradient()(FakeRequest())
      status(result) must equalTo(OK)
      contentAsString(result) must contain("[\"#")
    }
  }

}