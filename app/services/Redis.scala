package services

import play.api.libs.json._
import redis.clients.jedis.Jedis

import scala.collection.JavaConverters._

object Redis {

  val host = "localhost"

  def getVals(myKey: String): List[String] = {
    val jedis = new Jedis(host)
    jedis.lrange(myKey, 0, -1).asScala.toList
  }

  def addVals(myKey: String, myVal1: String, myVal2: String) = {
    val jedis = new Jedis(host)
    jedis.lpush(myKey, myVal1, myVal2)
  }

  def delVals(myKey: String) = {
    val jedis = new Jedis(host)
    jedis.del(myKey)
  }

  def addColor(myVal1: String, myVal2: String) = {
    val jedis = new Jedis(host)
    val myKey = jedis.incr("primary")
    jedis.lpush("color" + myKey, myVal1, myVal2)
  }

  def getAllColors(): JsValue = {
    val jedis = new Jedis(host)
    val keys = jedis.keys("color*").asScala.toSet
    var list = List[Colors]()
    for (key <- keys) {
      val colors = jedis.lrange(key, 0, -1)
      val cObj = Colors(colors.get(0), colors.get(1),"")
      list = cObj +: list
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
          "endColor" -> JsString(color.endColor)
        )))
    jsArray
  }
}


case class Colors(startColor: String, endColor: String, key: String)
