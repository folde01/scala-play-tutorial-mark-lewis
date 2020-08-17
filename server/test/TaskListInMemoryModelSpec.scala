import models.TaskListInMemoryModel
import org.scalatestplus.play._

class TaskListInMemoryModelSpec extends PlaySpec {
  "TaskListInMemoryModel" must {
    "do valid login for default user" in {
      TaskListInMemoryModel.validateUser("mark", "pass") mustBe true
    }

    "reject login with wrong password" in {
      TaskListInMemoryModel.validateUser("mark", "password") mustBe false
    }

    "reject login with wrong username" in {
      TaskListInMemoryModel.validateUser("markk", "password") mustBe false
    }

    "get correct default tasks" in {
      TaskListInMemoryModel.getTasks("mark") mustBe List("make vids", "eat", "sleep", "code")
    }
  }
}
