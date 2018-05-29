package controllers

import database.UserRepo
import javax.inject.{Inject, Singleton}
import models.{Login, User}
import play.api.db.Database
import play.api.libs.json.{JsError, JsPath, JsSuccess, Reads}
import play.api.libs.functional.syntax._
import play.api.mvc.{AbstractController, Action, ControllerComponents}

@Singleton
class LoginController @Inject()(db: Database, cc: ControllerComponents) extends AbstractController(cc) {
  val userRepo: UserRepo = new UserRepo(db)

  def authenticate = Action { request =>
    request.session.get("connected").map {
      user => Ok(user)
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }
  }

  def login = Action { request =>
    request.body.asJson.map { json =>
      json.validate[Login] match {
        case s: JsSuccess[Login] => {
          userRepo.getUser(s.value.username) match {
            case Some(u) => {
              if (s.value.username.equals(u.username) && s.value.pass.equals(u.pass)) {
                Ok(u.username).withSession("connected" -> u.username)
              } else {
                BadRequest("Wrong password")
              }
            }
            case None => {
              BadRequest("Wrong user info")
            }
          }
        }
        case e: JsError => {
          BadRequest("wrong format")
        }
      }
    }.getOrElse {
      BadRequest("lol")
    }
  }

  def logout = Action { request =>
    Ok("You are logged out").withNewSession
  }

  def register = Action { request =>
    Ok("Not complete")
  }

  implicit val loginRead: Reads[Login] = (
    (JsPath \ "user").read[String] and
    (JsPath \ "pass").read[String]
  )(Login.apply _)

  implicit val registerRead: Reads[User] = (
    (JsPath \ "user").read[String] and
    (JsPath \ "pass").read[String] and
    (JsPath \ "email").read[String]
  )(User.apply _)

}
