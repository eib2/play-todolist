package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.Task

object Application extends Controller {

	val taskForm = Form(
		"label" -> nonEmptyText
	)
	
	implicit val taskWrites:Writes[Task] = (
		(JsPath\"id").write[Long] and
		(JsPath\"label").write[String]
	)(unlift(Task.unapply))
	
	def index = Action {
		Ok(views.html.index(Task.all(), taskForm))
	}
	
	def tasks = Action {
		val json = Json.toJson(Task.all())
		Ok(json)
	}
	
	def newTask = Action { implicit request =>
		taskForm.bindFromRequest.fold(
			errors => BadRequest(views.html.index(Task.all(), errors)),
			label => {
				val json = Json.obj(
					"label" -> Json.toJson(Task.create(label))
				)
				Created(json)
			}
		)
	}

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

	






}
