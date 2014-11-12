package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.util.{Date}
import models._

case class TaskData(label: String, login: String, enddate: Option[Date], category: String)

object Application extends Controller {

	val taskForm = Form(
		mapping(
			"label" -> nonEmptyText,
			"login" -> nonEmptyText,
			"enddate" -> optional(date("YYYY-MM-DD")),
			"category" -> nonEmptyText
		)(TaskData.apply)(TaskData.unapply)
	)
	
	implicit val taskWrites:Writes[Task] = (
		(JsPath\"id").write[Long] and
		(JsPath\"label").write[String] and
		(JsPath\"usertask").write[String] and
		(JsPath\"enddate").write[Option[Date]] and
		(JsPath\"category").write[String]
	)(unlift(Task.unapply))
	
	def index = Action {
		Ok(views.html.index(Task.all(), taskForm))
	}
	
	def tasks = Action {
		val json = Json.toJson(Task.all())
		Ok(json)
	}
	
	def getTask(id: Long) = Action {
		Task.getTask(id) match {
			case Some(i) => {
				val json = Json.toJson(Task.getTask(id))
				Ok(json)
			}
			case None => NotFound
		}
	}
	/*
	def newTask = Action { implicit request =>
		taskForm.bindFromRequest.fold(
			errors => BadRequest(views.html.index(Task.all(), errors)),
			label => {
				val json = Json.obj(
					"label" -> Json.toJson(Task.create(label.toString()))
				)
				Created(json)
			}
		)
	}
*/

def newTask = newUserTask("Anonymous")

	def deleteTask(id: Long) = Action {
		Task.delete(id)
		Redirect(routes.Application.tasks)
	}

	def userTasks(login: String) = Action {
		Task.getUser(login) match {
			case Some(i) => {
				val json = Json.toJson(Task.allUser(i))
				Ok(json)
			}
			case None => NotFound
		}
	}
/*
	def newUserTask(label: String, login: String) = Action {
		Task.getUser(login) match {
			case Some(i) => {
				val json = Json.obj(
					"label" -> Json.toJson(Task.createUserTask(label, i))
				)
				Created(json)
			}
			case None => NotFound
		}
	}
*/
	def newUserTask(login: String) = newTaskUserDate(login, "")

	def dateToOptionDate(param: Date): Option[Date] = {
		Some(param)
	}
	
	def newTaskUserDate(login: String, enddate: String) = Action { implicit c => 
		var dateFormat = new java.text.SimpleDateFormat("YYYY-MM-DD")
		var dateParam: Option[Date] = None
		
		if(!enddate.isEmpty()){
			var date = dateFormat.parse(enddate)
			dateParam = dateToOptionDate(date)
		}
		
		taskForm.bindFromRequest.fold(
			errors => BadRequest("Error en la petición"),
			taskData => Task.getUser(login) match {
				case Some(i) => {
					if(Category.exists(taskData.category, login)){
						val id: Long = Task.createUserTaskDateCategory(taskData.label, login, taskData.category, dateParam)
						val task = Task.getTask(id)
						Created(Json.toJson(task))
					}
					else NotFound("No existe la categoria " + taskData.category + " para el usuario " + login)
				}
				
				case None => BadRequest("No existe el propietario de la tarea: " + login)
			}
		)
	}
	
	/*
	def newTaskUserDate(label: String, login: String, enddate: String) = Action {
		var date = dateFormat.parse(enddate)
		var dateParam = dateToOptionDate(date)
		
		Task.getUser(login) match {
			case Some(i) => {
				val json = Json.obj(
					"label" -> Json.toJson(Task.createUserTaskDate(label, i, dateParam))
				)
				Created(json)
			}
			case None => NotFound
		}
	}
	*/
	def tasksUserDate(login: String, enddate: String) = Action {
		var dateFormat = new java.text.SimpleDateFormat("YYYY-MM-DD")
		var date = dateFormat.parse(enddate)
		var dateParam = dateToOptionDate(date)
		
		Task.getUser(login) match {
			case Some(i) => {
				val json = Json.toJson(Task.allUserDate(i, dateParam))
				Ok(json)
			}
			case None => NotFound
		}
	}
	
	def tasksUserBeforeDate(beforedate: String) = Action {
		var dateFormat = new java.text.SimpleDateFormat("YYYY-MM-DD")
		var date = dateFormat.parse(beforedate)
		var dateParam = dateToOptionDate(date)
		val json = Json.toJson(Task.allBeforeDate(dateParam))
		Ok(json)
	}

	def allCategoryUser(user: String, category: String) = Action {
		Task.getUser(user) match {
			case Some(i) => {
				if(Category.exists(category, user)){
					val json = Json.toJson(Task.allCategoryUser(user, category))
					Ok(json)
				}
				else NotFound("No existe la categoría " + category + " del usuario " + user)
			}
			case None => NotFound
		}
	}

	def modifyTaskCategory(id: Long, category: String) = Action {
		Task.getTask(id) match {
			case Some(i) => {
				val correct = Task.modifyCategory(id, category)
				if(correct){
					val json = Json.toJson(Task.getTask(id))
					Ok(json)
				}
				else NotFound("No se ha modificado la tarea")
			}
			
			case None => NotFound("Tarea " + id + " no encontrada")
		}
	}




























}
