package controllers

import database.PostRepo
import javax.inject.{Inject, Singleton}
import models.BlogPost
import play.api.db.Database
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class BlogController @Inject()(db: Database, cc: ControllerComponents) extends AbstractController(cc) {
  val postRepo = new PostRepo(db)

  def getPosts(page: Int, pageSize: Int) = Action { request =>
    Ok(postRepo.select(page, pageSize))
  }

  def newPost = Action { request =>
    request.session.get("connected").map { user =>
      request.body.asJson.map { json =>
        json.validate[BlogPost] match {
          case s: JsSuccess[BlogPost] => {
            if (postRepo.insert(s.value)) {
              Ok("Post inserted")
            } else {
              BadRequest("Something went wrong with insert")
            }
          }
          case e: JsError => {
            BadRequest("Wrong format")
          }
        }
      }.getOrElse {
        BadRequest("something went wrong with the request")
      }
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }

  }

  def getPost(id: String) = Action { request =>
    postRepo.select(id) match {
      case Some(json) => Ok(json)
      case None => NotFound("No post with that id")
    }
  }

  def deletePost(id: String) = Action { request =>
    //TODO deletePost
    BadRequest("Not implemented")
  }

  implicit val blogPostRead: Reads[BlogPost] = (
    (JsPath \ "user").read[String] and
    (JsPath \ "header").read[String] and
    (JsPath \ "content").read[String]
  )(BlogPost.apply _)

}

