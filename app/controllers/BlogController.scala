package controllers

import javax.inject.{Inject, Singleton}
import models.BlogPost
import play.api.db.Database
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class BlogController @Inject()(db: Database, cc: ControllerComponents) extends AbstractController(cc) {

  def getPosts = Action { request =>
    db.withConnection { conn =>
      val rs = conn.createStatement.executeQuery("SELECT * from Posts")

      val json = Json.toJson(Iterator.continually((rs.next(), rs)).takeWhile(_._1).map { r =>
        JsObject(Seq(
          "id" -> JsNumber(r._2.getInt("id")),
          "user" -> JsString(r._2.getString("username")),
          "header" -> JsString(r._2.getString("header")),
          "content" -> JsString(r._2.getString("content"))
        ))
      }.toList)

      Ok(json)
    }
  }

  def newPost = Action { request =>
    request.session.get("connected").map { user =>
      request.body.asJson.map { json =>
        json.validate[BlogPost] match {
          case s: JsSuccess[BlogPost] => {
            db.withConnection { conn =>
              val st = conn.prepareStatement("INSERT INTO Posts (username, header, content) VALUES (?,?,?)")
              st.setObject(1, s.value.username)
              st.setObject(2, s.value.header)
              st.setObject(3, s.value.content)
              st.executeUpdate()
              Ok("Post inserted")
            }
          }
          case e: JsError => {
            BadRequest("Wrong format")
          }
        }
      }.getOrElse {
        BadRequest("something went wring with the request")
      }
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }

  }

  def getPost(id: String) = Action { request =>
    Ok
  }

  implicit val blogPostRead: Reads[BlogPost] = (
    (JsPath \ "user").read[String] and
    (JsPath \ "header").read[String] and
    (JsPath \ "content").read[String]
  )(BlogPost.apply _)

}

