package controllers

import javax.inject.{Inject, Singleton}
import models.TaskListInMemoryModel
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class TaskList1 @Inject()(cc: ControllerComponents) extends AbstractController(cc)  {

  def login() = Action {
    Ok(views.html.login1())
  }

  def validateLoginGet(username: String, password: String) = Action {
    Ok(s"$username logged in with $password")
  }

  def validateLoginPost = Action { request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.validateUser(username, password))
        Redirect(routes.TaskList1.taskList()) // reverse routing
      else Redirect(routes.TaskList1.login())
    }.getOrElse(Redirect(routes.TaskList1.login()))
  }

  def createUser = Action { request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.createUser(username, password))
        Redirect(routes.TaskList1.taskList()) // reverse routing
      else Redirect(routes.TaskList1.login())
    }.getOrElse(Redirect(routes.TaskList1.login()))
  }

  def taskList = Action {
//    val tasks = List("task1", "task2", "tasks3", "sleep", "eat")
    val username = "mark"
    val tasks = TaskListInMemoryModel.getTasks(username)
    Ok(views.html.tasklist1(tasks))
  }

  def product(prodName: String, prodNum: Int) = Action {
    Ok(s"name: $prodName, num: $prodNum")
  }

}
