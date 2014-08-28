package services

import javax.inject.{Inject, Named, Singleton}

import play.api.libs.json._
import redis.clients.jedis.Jedis

import scala.collection.JavaConverters._

abstract class Db()

@Singleton
class Redis @Inject()(@Named("Redis Host") host: String) extends Db() {

  var jedisInstance: Jedis = null

  def setJedis(jedis: Jedis) = {
    jedisInstance = jedis
  }

  def getJedis(): Jedis = {
    if (jedisInstance == null)
      jedisInstance = new Jedis(host)
    jedisInstance
  }

  def delVals(myKey: String) = {
    getJedis().del(myKey)
  }

  def addGradient(myVal1: String, myVal2: String) = {
    val myKey = getJedis().incr("primary")
    getJedis().lpush("color" + myKey, myVal1, myVal2)
  }

  def getAllGradients(): JsValue = {
    val keys = getJedis().keys("color*").asScala.toSet
    var list = List[Colors]()
    for (key <- keys) {
      val colors = getJedis().lrange(key, 0, -1)
      if (colors.size() != 0) {
        val cObj = Colors(colors.get(0), colors.get(1), key)
        list = cObj +: list
      }
    }
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
