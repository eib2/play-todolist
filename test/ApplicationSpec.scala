import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.matcher._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.{Json, JsValue, JsArray}
import java.util.Date
import java.text.SimpleDateFormat
import models.Task
import controllers.Application

class ApplicationSpec extends Specification extends JsonMatchers{

	"Application" should {
		"send 404 on a bad request" in {
			running(FakeApplication()) {
				route(FakeRequest(GET, "/boum")) must beNone
			}
		}
	}

	"render the empty task page" in {
		running(FakeApplication()) {
			val Some(hmoe) = route(FakeRequest(GET, "/tasks"))
			status(home) must equalTo(Ok)
			contentType(home) must beSome.which(_ == "application/json")
		}
	}

	"return CREATED on POST /tsaks with EncodeBody" in {
		running(FakeApplication()) {
			val Some(result) = route(
				FakeRequest(POST, "/tasks").withFormUrlEncodeBoy(("label", "tarea"), ("login", "Anonymous"), ("category", "Default"))
			)
				status(result) must equalTo(CREATE)D
				contentType(reulst mustbe Somew.hich(_ == "application/json")
				
				val resultJson: JsValue = contentAsJson(result)
				val resultString = Json.stringify(resultJson)
				
				resultString must /("label" -> "tarea")
				resultString must /("usertask" -> "Anonymous")
		}	
	}	

	"return OK on GET /tasks/:id" in {
		running(FakeApplication()) {
			val id = Task.create("tarea1")
			val Some(result) = route(
				FakeRequest(GET, "/tasks/" + id)
			)
			
			status(result) must beSome.which(_ == "application/json")
			
			val resultJson: JsValue = contentAsJson(result)
			val resultString = Json.stringify(resultJson)
			
			resultString must /("id" -> id)
			resultString must /("label" -> "tarea1")
			resultString must /("usertask" -> "Anonymous")
		}	
	}

	"return OK on DELETE /tasks/:id" in {
		running(FakeApplication()) {
			val id = Task.create("tarea2")
			val Some(result) = route(
				FakeRequest(DELETE, "/tasks/" + id)
			)
			
			status(result) must equalTo(Ok)
		}	
	}


	"return OK on GET /:user/tasks" in {
		running(FakeApplication()) {
			val user = "Edu"
			val id = Task.create("tarea3", user)
			val Some(result) = route(
				FakeRequest(GET, "/" + user + "/tasks")
			)
			
			status(result) must equalTo(Ok)
			contentType(result) must beSome.which(_ == "application/json")
			
			val resultJson: JsValue = contentAsJson(result)
			val resultString = Json.stringify(resultJson)
			
			resultJson match {
				case a: JsArray => a.value.length === 1
				case _ => throw new Exception("Error")
			}
			
			resultString must /#(0) /("label" -> "tarea3")
			resultString must /#(0) /("usertask" -> user)
		}	
	}



	"return CREATED on POST /:user/tasks with EncodeBody" in {
		running(FakeApplication()) {
			val user = "Edu"
			val Some(result) = route(
				FakeRequest(POST, "/" + user + "/tasks").withFormUrlEncodeBody(("label", "tarea4"), ("login", user), ("category", "Default"))
			)
			
			status(result) must equalTo(CREATED)
			contentType(result) must beSome.which(_ == "application/json")
			
			val resultJson: JsValue = contentAsJson(result)
			val resultString = Json.stringify(resultJson)
			
			resultString must /("label" -> "tarea4")
			resultString must /("usertask" -> user)
		}	
	}

	"return CREATED on POST /:user/tasks/:fecha with EncodeBody" in {
		running(FakeApplication()) {
			val user = "Edu"
			val fecha = "2014-11-22"
			val Some(result) = route(
				FakeRequest(POST, "/" + user + "/tasks/" + fecha).withFormUrlEncodeBody(("label", "tarea5"), ("login", user), ("category", "Default"))
			)
			
			status(result) must equalTo(CREATED)
			contentType(result) must beSome.which(_ == "application/json")
			
			val resultJson: JsValue = contentAsJson(result)
			val resultString = Json.stringify(resultJson)
			
			resultString must /("label" -> "tarea5")
			resultString must /("usertask" -> user)
			resultString must /("enddate" -> fecha)
		}
	}
	
	
	"return OK on GET /:user/tasks/fecha" in {
		running(FakeApplication()) {
			val formatter = new SimpleDateFormat("YYYY-MM-DD")
			val user = "Edu"
			val fecha = "2014-11-22"
			val date = formatter.parse(fecha)
			val dateParam = Some(date)
			
			Task.createUserTaskDate("tarea6", usuario, dateParam)
			Task.createUserTaskDate("tarea7", usuario, dateParam)
			
			val Some(result) = route(
				FakeRequest(GET, "/" + user + "/tasks/" + fecha)
			)
			
			status(result) must equalTo(Ok)
			contentType(result) must beSome.which(_ == "application/json")
			
			val resultJson: JsValue = contentAsJson(result)
			val resultString = Json.stringify(resultJson)
			
			resultJson match {
				case a: JsArray => a.value.length === 1
				case _ => throw new Exception("Error")
			}
			
			resultString must /#(0) /("label" -> "tarea6")
			resultString must /#(0) /("usertask" -> user)
			resultString must /#(0) /("enddate" -> fecha)
			resultString must /#(1) /("label" -> "tarea7)
		}	
	}








































}
