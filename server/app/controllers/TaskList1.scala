package controllers

import javax.inject.{Inject, Singleton}
import models.TaskListInMemoryModel
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.mvc.{AbstractController, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}

case class LoginData(username: String, password: String)

@Singleton
class TaskList1 @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  val loginForm: Form[LoginData] = Form(mapping(
    "Username" -> text(3, 10),
    "Password" -> text(8)
  )(LoginData.apply)(LoginData.unapply))

  def login() = Action { implicit request =>
    Ok(views.html.login1(loginForm))
  }

  def validateLoginGet(username: String, password: String) = Action {
    Ok(s"$username logged in with $password")
  }

  def validateLoginPost = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.validateUser(username, password))
        Redirect(routes.TaskList1.taskList()).withSession("username" -> username) // reverse routing
      else Redirect(routes.TaskList1.login()).flashing("error" -> "Invalid username/password combination")
    }.getOrElse(Redirect(routes.TaskList1.login()))
  }


  def createUser = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.createUser(username, password))
        Redirect(routes.TaskList1.taskList()).withSession("username" -> username) // reverse routing
      else Redirect(routes.TaskList1.login()).flashing("error" -> "user creation failed")
    }.getOrElse(Redirect(routes.TaskList1.login()))
  }

  def createUserForm = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login1(formWithErrors)),
      ld =>
        if (TaskListInMemoryModel.createUser(ld.username, ld.password)) {
          Redirect(routes.TaskList1.taskList()).withSession("username" -> ld.username) // reverse routing
        } else Redirect(routes.TaskList1.login()).flashing("error" -> "user creation failed")
    )
  }

  def taskList = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val tasks = TaskListInMemoryModel.getTasks(username)
      Ok(views.html.tasklist1(tasks))
    }.getOrElse(Redirect(routes.TaskList1.login()))
  }

  def logout = Action {
    Redirect(routes.TaskList1.login()).withNewSession

  }

  def addTask = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val task = args("newTask").head
        TaskListInMemoryModel.addTask(username, task)
        Redirect(routes.TaskList1.taskList())
      }.getOrElse(Redirect(routes.TaskList1.taskList()))
    }.getOrElse(Redirect(routes.TaskList1.login()))
  }

  def deleteTask = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val taskIndex = args("index").head.toInt
        TaskListInMemoryModel.removeTask(username, taskIndex)
        Redirect(routes.TaskList1.taskList())
      }.getOrElse(Redirect(routes.TaskList1.taskList()))
    }.getOrElse(Redirect(routes.TaskList1.login()))
  }

  def product(prodName: String, prodNum: Int) = Action {
    Ok(s"name: $prodName, num: $prodNum")
  }

}
