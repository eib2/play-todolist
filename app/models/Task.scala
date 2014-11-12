package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.{Date}

case class Task(id: Long, label: String, usertask: String, enddate: Option[Date], category: String)

object Task {
	
	val task = {
		get[Long]("id") ~
		get[String]("label") ~
		get[String]("usertask_fk") ~
		get[Option[Date]]("enddate") ~ 
		get[String]("categorytask_fk") map {
			case id~label~usertask~enddate~category => Task(id, label, usertask, enddate, category)
		}
	}
	
	def all(): List[Task] = DB.withConnection { implicit c =>
		SQL("SELECT * FROM task WHERE usertask_fk = 'Anonymous'").as(task *)
	}
	
	def getTask(id: Long): Option[Task] = DB.withConnection { implicit c =>
		SQL("select * from task where id = {id}").on(
			'id -> id
		).as(task.singleOpt)
	}
	
	def create(label: String): String = DB.withConnection { implicit c =>
		SQL("insert into task (label) values ({label})").on(
				'label -> label
				).executeUpdate()
				return label
	}
	
	def delete(id: Long): Int = DB.withConnection { implicit c =>
		SQL("delete from task where id = {id}").on(
				'id -> id
				).executeUpdate()
	}
	
	def getUser(login: String) : Option[String] = DB.withConnection { implicit c =>
		SQL("SELECT login FROM usertask WHERE login = {login}").on(
			'login -> login
		).as(scalar[String].singleOpt)
	}
	
	def allUser(login: String): List[Task] = DB.withConnection { implicit c =>
		SQL("SELECT * FROM task WHERE usertask_fk = {usuario}").on(
			'usuario -> login
		).as(task *)
	}
	
	def createUserTask(label: String, login: String): String = DB.withConnection { implicit c =>
		SQL("INSERT INTO task (usertask_fk, label) VALUES ({login}, {label})").on(
			'login ->login,
			'label -> label
		).executeUpdate()
		return label
	}
	
	def allUserDate(login: String, enddate: Option[Date]): List[Task] = DB.withConnection { implicit c=>
		SQL("SELECT * FROM task WHERE usertask_fk = {usuario} and enddate = {enddate}").on(
			'usuario -> login,
			'enddate -> enddate
		).as(task *)
	}
	
	def allBeforeDate(dateBefore: Option[Date]): List[Task] = DB.withConnection { implicit c=>
		SQL("SELECT * FROM task WHERE enddate < {enddate}").on(
			'dateBefore -> dateBefore
		).as(task *)
	}
	
	def createUserTaskDate(label: String, login: String, enddate: Option[Date]): Long = DB.withConnection { implicit c =>
		var date = Some(enddate)
		SQL("INSERT INTO task (usertask_fk, label, enddate) VALUES ({login}, {label}, {enddate})").on(
			'login -> login,
			'label -> label,
			'enddate -> enddate
		).executeUpdate()
	}
	
	def createUserTaskDateCategory(label: String, login: String, category: String, enddate: Option[Date]): Long = DB.withConnection { implicit c => 
		var date = Some(enddate)
	
		SQL("INSERT INTO task (usertask_fk, label, enddate, categorytask_fk) VALUES ({login}, {label}, {enddate}, {category})").on(
				'login -> login,
				'label -> label,
				'enddate -> enddate,
				'category -> category
			).executeUpdate()
	}

	def allCategoryUser(user: String, category: String): List[Task] = DB.withConnection { implicit c => 
		SQL("select * from task where usertask_fk = {usuario} and categorytask_fk = {category}").on(
			'usuario -> user,
			'category -> category
		).as(task *)
}
	
	def modifyCategory(id: Long, category: String): Boolean = DB.withConnection { implicit c =>
		val result: Int = SQL("update task set categorytask_fk = {category] where id = {id}").on(
			'category -> category,
			'id -> id
		).executeUpdate()
		
		result match {
			case 1 => true
			case _ => false
		}
	}
}







