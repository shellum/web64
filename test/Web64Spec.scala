import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class Web64Spec extends Specification {

  "Web64" should {
    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("Web64")
    }

    "encode a string" in new WithApplication {
      val home = route(FakeRequest(POST, "/encode").withFormUrlEncodedBody(("data", "asdf"))).get
      contentAsString(home) must beEqualTo("YXNkZg")
    }

    "decode a string" in new WithApplication {
      val home = route(FakeRequest(POST, "/decode").withFormUrlEncodedBody(("data", "YXNkZg"))).get
      contentAsString(home) must beEqualTo("asdf")
    }
  }


}
