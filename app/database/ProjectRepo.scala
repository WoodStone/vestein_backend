package database

import models.Project
import play.api.db.Database
import play.api.libs.json._

class ProjectRepo(db: Database) {

  def insert(project: Project): Boolean = {
    db.withConnection { conn =>
      val st = conn.prepareStatement("INSERT INTO Projects (header, meta, description, glink, imagelink) VALUES (?, ?, ? ,? ,?")
      st.setObject(1, project.header)
      st.setObject(2, project.meta)
      st.setObject(3, project.description)
      st.setObject(4, project.glink)
      st.setObject(5, project.imagelink)
      st.executeUpdate()
      return true
    }
  }

  def select(page: Int, pageSize: Int): JsValue = {
    val offset = pageSize * (page - 1)
    db.withConnection { conn =>
      val ps = conn.prepareStatement("SELECT id, header, meta, description, glink, imagelink FROM Projects ORDER BY id DESC LIMIT ? OFFSET ?")
      ps.setObject(1, pageSize)
      ps.setObject(2, offset)
      val rs = ps.executeQuery()


      return Json.toJson(Iterator.continually((rs.next(), rs)).takeWhile(_._1).map { r =>
        JsObject(Seq(
          "id" -> JsNumber(r._2.getInt("id")),
          "header" -> JsString(r._2.getString("header")),
          "meta" -> JsString(r._2.getString("meta")),
          "description" -> JsString(r._2.getString("description")),
          "glink" -> JsString(r._2.getString("glink")),
          "imagelink" -> JsString(r._2.getString("imagelink"))
        ))
      }.toList)
    }
  }

  def select(id: String): Option[JsValue] = {
    db.withConnection { conn =>
      val ps = conn.prepareStatement("SELECT * FROM Projects WHERE id = ?")
      ps.setObject(1, id)
      val rs = ps.executeQuery()

      if (rs.next()) {
        Option(
          JsObject(Seq(
            "id" -> JsNumber(rs.getInt("id")),
            "header" -> JsString(rs.getString("header")),
            "meta" -> JsString(rs.getString("meta")),
            "description" -> JsString(rs.getString("description")),
            "glink" -> JsString(rs.getString("glink")),
            "imagelink" -> JsString(rs.getString("imagelink"))
          ))
        )
      } else {
        None
      }
    }
  }

  def delete(id: String): Boolean = {
    true
  }

}
