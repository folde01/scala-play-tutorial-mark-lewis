import models.TaskListInMemoryModel
import org.scalatestplus.play._

class TaskListInMemoryModelSpec extends PlaySpec {
  "TaskListInMemoryModel" must {
    "do valid login for default user" in {
      TaskListInMemoryModel.validateUser("mark", "pass") mustBe(true)
    }

    "reject login with wrong password" in {
      TaskListInMemoryModel.validateUser("mark", "password") mustBe (false)
    }

    "reject login with wrong username" in {
      TaskListInMemoryModel.validateUser("markk", "password") mustBe(false)
    }

    "reject login with wrong username and password" in {
      TaskListInMemoryModel.validateUser("markk", "passwordd") mustBe(false)
    }

    "get correct default tasks" in {
      TaskListInMemoryModel.getTasks("mark") mustBe(List("make vids", "eat", "sleep", "code"))
    }

    "create new user with no tasks" in {
      TaskListInMemoryModel.createUser("mike", "pass") mustBe(true)
      TaskListInMemoryModel.getTasks("mike") mustBe(Nil)
    }

    "create new user with existing name" in {
      TaskListInMemoryModel.createUser("mark", "passssss") mustBe(false)
    }

    "add new task for default user" in {
      TaskListInMemoryModel.addTask("mark", "testing")
      TaskListInMemoryModel.getTasks("mark") must contain("testing")
    }

    "add new task for new user" in {
      TaskListInMemoryModel.addTask("mike", "faffing")
      TaskListInMemoryModel.getTasks("mike") must contain("faffing")
    }

    "remove task for default user" in {
      TaskListInMemoryModel.removeTask("mark", TaskListInMemoryModel.getTasks("mark").indexOf("eat"))
      TaskListInMemoryModel.getTasks("mark") must not contain ("eat")
    }

  }
}
