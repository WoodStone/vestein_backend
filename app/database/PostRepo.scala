package database

import java.text.{DateFormat, SimpleDateFormat}
import java.util.{Date, Locale, TimeZone}

import models.BlogPost
import play.api.db.Database
import play.api.libs.json._

/**
  * Created by veste
  * Date: 05.06.2018
  * Time: 22:39
  */
class PostRepo(db: Database) {

  def select(page: Int, pageSize: Int): JsValue = {
    val offset = pageSize * (page - 1)
    db.withConnection { conn =>
      val ps = conn.prepareStatement("SELECT id, username, timestamp, header, content FROM Posts ORDER BY id DESC LIMIT ? OFFSET ?")
      ps.setObject(1, pageSize)
      ps.setObject(2, offset)
      val rs = ps.executeQuery()

      return Json.toJson(Iterator.continually((rs.next(), rs)).takeWhile(_._1).map { r =>
        JsObject(Seq(
          "id" -> JsNumber(r._2.getInt("id")),
          "user" -> JsString(r._2.getString("username")),
          "timestamp" -> JsString(r._2.getString("timestamp")),
          "header" -> JsString(r._2.getString("header")),
          "content" -> JsString(r._2.getString("content"))
        ))
      }.toList)

    }
  }

  def select(id: String): Option[JsValue] = {
    db.withConnection { conn =>
      val ps = conn.prepareStatement("SELECT * FROM Posts WHERE id = ?")
      ps.setObject(1, id)
      val rs = ps.executeQuery()

      if (rs.next()) {
        Option(
          JsObject(Seq(
            "id" -> JsNumber(rs.getInt("id")),
            "user" -> JsString(rs.getString("username")),
            "timestamp" -> JsString(rs.getString("timestamp")),
            "header" -> JsString(rs.getString("header")),
            "content" -> JsString(rs.getString("content"))
          ))
        )
      } else {
        None
      }

    }
  }

  def insert(post: BlogPost): Boolean = {
    db.withConnection { conn =>
      val st = conn.prepareStatement("INSERT INTO Posts (username, timestamp, header, content) VALUES (?,?,?,?)")
      st.setObject(1, post.username)
      st.setObject(2, iso8601String)
      st.setObject(3, post.header)
      st.setObject(4, post.content)
      st.executeUpdate()
      return true
    }
  }

  def iso8601String: String = {
    val now: Date = new Date()
    val dateFormat: DateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.US)
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    return dateFormat.format(now)
  }

}
