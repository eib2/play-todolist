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
//		Redirect(routes.Application.tasks)
	}
	
	def tasks = Action {
		val json = Json.toJson(Task.all())
		Ok(json)
//		Ok(views.html.index(Task.all(), taskForm))
	}
	
	def newTask = Action { implicit request =>
		taskForm.bindFromRequest.fold(
			errors => BadRequest(views.html.index(Task.all(), errors)),
			label => {
				val json = Json.obj(
					"label" -> Json.toJson(Task.create(label))
				)
				Created(json)	
//				Redirect(routes.Application.tasks)
			}
		)
	}

	def deleteTask(id: Long) = Action {
		Task.delete(id)
		Redirect(routes.Application.tasks)
	}

}
