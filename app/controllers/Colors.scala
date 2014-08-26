package controllers

import javax.inject.Inject

import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.Json
import play.api.mvc._
import services.Redis

import scala.util.Random

class Colors @Inject()(redis: Redis) extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.colors(getRandomHexColor, getRandomHexColor))
  }

  def save = Action { implicit request =>
    val startColor = userForm.bindFromRequest.get.startColor
    val endColor = userForm.bindFromRequest.get.endColor
    redis.addGradient(startColor, endColor)
    Ok("")
  }

  def getAllColors = Action { implicit request =>
    val a = (redis.getAllGradients())
    Ok(a).as("text")
  }

  def getRandomHexColor: String = {
    val random = new Random
    var hexString = ""
    for (i <- 1 to 3) hexString += (random.nextInt(245) + 10).toHexString
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