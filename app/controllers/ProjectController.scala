package controllers

import database.ProjectRepo
import javax.inject.{Inject, Singleton}
import models.Project
import play.api.db.Database
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class ProjectController @Inject()(db: Database, cc: ControllerComponents) extends AbstractController(cc) {
  val projectRepo: ProjectRepo = new ProjectRepo(db)

  def list(page: Int, pageSize: Int) = Action { request =>
    Ok(projectRepo.select(page, pageSize))
  }

  def create = Action { request =>
    request.session.get("connected").map { user =>
      request.body.asJson.map { json =>
        json.validate[Project] match {
          case s: JsSuccess[Project] => {
            if (projectRepo.insert(s.value)) {
              Ok("Project is added")
            } else {
              BadRequest("Something went wrong with the insert operation.")
            }
          }
          case e: JsError => BadRequest("Wrong format.")
        }
      }.getOrElse {
        BadRequest("Something wrong with the request.")
      }
    }.getOrElse {
      Unauthorized("Oops, you are not connected.")
    }
  }

  def retrieve(id: String) = Action { request =>
    projectRepo.select(id) match {
      case Some(p) => Ok(p)
      case None => BadRequest
    }
  }

  def delete(id: String) = Action { request =>
    request.session.get("connected").map { user =>
      Ok
    }.getOrElse {
      Unauthorized("Oops, you are not connected.")
    }
  }

  def update(id: String) = Action { request =>
    Ok
  }

  implicit val projectRead: Reads[Project] = (
      (JsPath \ "header").read[String] and
      (JsPath \ "meta").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "glink").read[String] and
      (JsPath \ "imagelink").read[String]
    )(Project.apply _)

}
