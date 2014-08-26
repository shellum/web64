import org.specs2.execute.Results
import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import play.api.libs.json.{JsString, JsObject}
import play.api.mvc.SimpleResult
import play.api.test.{FakeRequest, PlaySpecification}
import redis.clients.jedis.Jedis
import services.Redis

import scala.collection.JavaConverters._
import scala.concurrent.Future

/**
 * Created by cameron.shellum on 8/25/14.
 */
class RedisSpec  extends PlaySpecification with Results with Mockito {

  class RedisScope extends Scope {
    val jedisMock = mock[Jedis]
    val testStartColor = "#000000"
    val testEndColor = "#ffffff"
    val testHost = "localhost"
    val testKey = "testKey"
    var testGradient = new java.util.ArrayList[String]
    testGradient.add(testStartColor)
    testGradient.add(testEndColor)
    jedisMock.lrange(any[String], any[Int], any[Int]) returns testGradient
    val redis = new Redis(testHost)
    redis.setJedis(jedisMock)
    val redisMock = mock[Redis]
  }

  "Redis" should {

    "Delete a gradient" in new RedisScope {
      redis.delVals(testKey)
      there was one(jedisMock).del(any[String])
    }

    "Add a gradient" in new RedisScope {
      redis.addGradient(testStartColor,testEndColor)
      there was one(jedisMock).lpush(any[String],any[String],any[String])
    }

    "Get all the stored gradients" in new RedisScope {
      val set = new java.util.TreeSet[String]
      set.add(testKey + "a")
      set.add(testKey + "b")
      set.add(testKey + "c")
      jedisMock.keys(any[String]) returns set
      jedisMock.lrange(any[String],any[Int],any[Int]) returns testGradient
      val jsVal = redis.getAllGradients()
      jsVal.toString() must beEqualTo("[{\"startColor\":\"#000000\",\"endColor\":\"#ffffff\",\"key\":\"testKeyc\"},{\"startColor\":\"#000000\",\"endColor\":\"#ffffff\",\"key\":\"testKeyb\"},{\"startColor\":\"#000000\",\"endColor\":\"#ffffff\",\"key\":\"testKeya\"}]")
    }
  }


}
