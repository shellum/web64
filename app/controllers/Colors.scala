package controllers

import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.Redis
import scala.runtime.RichInt

import scala.util.Random

object Colors extends Controller {

  def index = Action {
//    val l = Redis.getAllColors()
//    val list = List.empty[String] ++ l
//    if (list.size > 0) Ok(views.html.colors(list(0), list(1)))

     Ok(views.html.colors(getRandomHexColor, getRandomHexColor))
  }

  def addColor(startColor: String, endColor: String) = {
    Redis.addColor(startColor, endColor)
  }

  def save = Action { implicit request =>
    val startColor = userForm.bindFromRequest.get.startColor
    val endColor = userForm.bindFromRequest.get.endColor
    Redis.addColor(startColor, endColor)
    Ok()
  }

  def getAllColors = Action {implicit request =>
    val a = (Redis.getAllColors())
    Ok(a).as("text")
  }

  def getRandomHexColor: String = {
    val random = new Random
    var hexString = ""
    for (i <- 1 to 3) hexString += (random.nextInt(246)+15).toHexString
    "#" + hexString
  }

  def removeRandomGradient

  def getRandomGradient = Action { implicit request =>
    Ok(Json.toJson(Seq(getRandomHexColor,getRandomHexColor)))
  }

  val userForm = Form(
    mapping(
      "startColor" -> text,
      "endColor" -> text
    )(FormColors.apply)(FormColors.unapply)
  )
}

case class FormColors(startColor: String, endColor: String)