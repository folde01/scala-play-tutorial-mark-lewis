package models

import scala.collection.mutable

object TaskListInMemoryModel {

  private val users = mutable.Map[String, String](
    "mark" -> "pass"
  )

  def validateUser(username: String, password: String): Boolean = {
//    users(username) == password
    users.get(username).map(_ == password).getOrElse(false)
  }

  def createUser(username: String, password: String): Boolean = ???

  def getTasks(username: String): Seq[String] = ???

  def addTask(username: String, task: String): Unit = ???

  def removeTask(username: String, index: Int): Boolean = ???

}
