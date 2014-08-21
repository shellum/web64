package controllers

import org.apache.commons.codec.binary.Base64
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._

object Web64 extends Controller {

  def index = Action {
    Ok(views.html.index("Enter text here for web safe base64 encoding/decoding."))
  }

  def encode = Action { implicit request =>
    val (data) = userForm.bindFromRequest.get.data
    val encodedData = Base64.encodeBase64URLSafeString(data.getBytes())
    Ok(encodedData).as("text")
  }

  def decode = Action { implicit request =>
    val (data) = userForm.bindFromRequest.get.data
    val decodedData = new String(Base64.decodeBase64(data.getBytes()))
    Ok(decodedData).as("text")
  }

  val userForm = Form(
    mapping(
      "data" -> text
    )(Form64.apply)(Form64.unapply)
  )

}

case class Form64(data: String)