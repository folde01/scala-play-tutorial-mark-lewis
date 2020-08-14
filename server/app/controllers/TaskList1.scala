package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class TaskList1 @Inject()(cc: ControllerComponents) extends AbstractController(cc)  {
  def taskList = Action {
    val tasks = List("task1", "task2", "tasks3", "sleep", "eat")
    Ok(views.html.tasklist1(tasks))
  }

  def product(prodName: String, prodNum: Int) = Action {
    Ok(s"name: $prodName, num: $prodNum")
  }
}
