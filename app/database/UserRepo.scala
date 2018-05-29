package database

import models.User
import play.api.db.Database

class UserRepo(db: Database) {

  def getUser(username: String): Option[User] = {
    db.withConnection { conn =>
      val st = conn.prepareStatement("SELECT * from Users WHERE username=?")
      st.setObject(1, username)
      val rs = st.executeQuery()
      if (rs.next()) {
        val username = rs.getString("username")
        val pass = rs.getString("pass")
        val email = rs.getString("email")
        Option(User(username, pass, email))
      } else {
        None
      }
    }
  }

}
