package services

import javax.inject.{Inject, Named, Singleton}

import play.api.libs.json._
import redis.clients.jedis.{JedisPoolConfig, JedisPool, Jedis}

import scala.collection.JavaConverters._

abstract class Db()

@Singleton
class Redis @Inject()(@Named("Redis Host") host: String) extends Db() {

  var jedisPool: JedisPool = null

  def setJedis(jedisPool: JedisPool) = {
    this.jedisPool = jedisPool
  }

  def getJedis(): Jedis = {
    if (jedisPool == null)
      jedisPool = new JedisPool(new JedisPoolConfig(), host)
    jedisPool.getResource
  }

  def delVals(myKey: String) = {
    val jedis = getJedis()
    jedis.del(myKey)
    jedis.close()
  }

  def addGradient(myVal1: String, myVal2: String) = {
    val jedis = getJedis()
    val myKey = jedis.incr("primary")
    jedis.lpush("color" + myKey, myVal1, myVal2)
    jedis.close()
  }

  def getAllGradients(): JsValue = {
    val jedis = getJedis()
    val keys = jedis.keys("color*").asScala.toSet
    var list = List[Colors]()
    for (key <- keys) {
      val colors = jedis.lrange(key, 0, -1)
      if (colors.size() != 0) {
        val cObj = Colors(colors.get(0), colors.get(1), key)
        list = cObj +: list
      }
    }
    jedis.close()
    colorsFormat.writes(list)
  }

}

object colorsFormat {
  def writes(colors: List[Colors]): JsValue = {
    var jsArray = JsArray()
    for (color <- colors)
      jsArray = jsArray.append(
        JsObject(Seq(
          "startColor" -> JsString(color.startColor),
          "endColor" -> JsString(color.endColor),
          "key" -> JsString(color.key)
        )))
    jsArray
  }
}

case class Colors(startColor: String, endColor: String, key: String)
