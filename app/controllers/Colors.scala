package controllers

import javax.inject.Inject

import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.Json
import play.api.mvc._
import services.Redis

import scala.util.Random

class Colors @Inject()(redis: Redis) extends Controller {

  def index = Action {
    Ok(views.html.colors(getRandomHexColor, getRandomHexColor))
  }

  def addColor(startColor: String, endColor: String) = {
    redis.addColor(startColor, endColor)
  }

  def save = Action { implicit request =>
    val startColor = userForm.bindFromRequest.get.startColor
    val endColor = userForm.bindFromRequest.get.endColor
    redis.addColor(startColor, endColor)
    Ok("")
  }

  def getAllColors = Action { implicit request =>
    val a = (redis.getAllColors())
    Ok(a).as("text")
  }

  def getRandomHexColor: String = {
    val random = new Random
    var hexString = ""
    for (i <- 1 to 3) hexString += (random.nextInt(246) + 15).toHexString
    "#" + hexString
  }

  def removeGradient(key: String) = Action { implicit request =>
    redis.delVals(key)
    Ok("")
  }

  def getRandomGradient = Action { implicit request =>
    Ok(Json.toJson(Seq(getRandomHexColor, getRandomHexColor)))
  }

  val userForm = Form(
    mapping(
      "startColor" -> text,
      "endColor" -> text
    )(FormColors.apply)(FormColors.unapply)
  )
}

case class FormColors(startColor: String, endColor: String)