package controllers

import com.mongodb.casbah.Imports._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import com.fasterxml.jackson.databind.JsonNode
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsResult
import play.api.libs.json.JsValue
import play.api.libs.json.Format
import play.api.libs.json.Json

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def listProducts(id: Int) = Action {
    val json = Query.query(id)
    Ok(json).as("application/json")
  }

  //  val userForm = Form(
  //    mapping(
  //      "id" -> number,
  //      "title" -> text,
  //      "pricing" -> mapping(
  //        "cost" -> number,
  //        "price" -> number,
  //        "promoPrice"  -> number,
  //        "savings" -> number,
  //        "onSale" -> number
  //      )(Pricing.apply)(Pricing.unapply)
  //    	)(Product.apply)(Product.unapply))

  def addProduct() = Action { implicit request =>
    //    userForm.bindFromRequest.fold(
    //    formWithErrors => BadRequest("Oh noes, invalid submission!"),
    //    value => Ok("created: " + value)
    //    )
    Ok("Success")
  }

  //  implicit val objectMapFormat = new Format[Map[String, String]] {
  //
  //  def writes(map: Map[String, Object]): JsValue =
  //    Json.obj(
  //      "val1" -> map("val1").asInstanceOf[String],
  //      "val2" -> map("val2").asInstanceOf[String]
  //    )
  //
  //  def reads(jv: JsValue): JsResult[Map[String, Object]] =
  //    JsSuccess(Map("val1" -> (jv \ "val1").as[String], 
  //        "val2" -> (jv \ "val2").as[String]))
  //}

  // curl   --header "Content-type: application/json" --request POST  --data '{"name": "Guillaume"}' http://localhost:9000/sayHello
  def sayHello2 = Action { request =>
    request.body.asJson.map { json =>
      (json \ "name").asOpt[String].map { name =>
        Ok("Hello " + name)
      }.getOrElse {
        BadRequest("Missing parameter [name]")
      }
    }.getOrElse {
      BadRequest("Expecting Json data")
    }
  }

  def sayHello = Action(parse.json) { request =>
    (request.body \ "name").asOpt[String].map { name =>
      println("hello " + name)
      Ok("Hello " + name)
    }.getOrElse {
      BadRequest("Missing parameter [name]")
    }
  }
}

case class Product(id: Int, title: String, pricing: Pricing)
case class Pricing(cost: Int, price: Int, promoPrice: Int, savings: Int, onSale: Int)

object Query {
  def query(id: Int) = {
    println("query id " + id)
    val mongoClient = MongoClient();
    val db = mongoClient("ctlg")
    val col = db("catalogs")
    val q = "/^" + id.toString + ".*/.test(this.id)"
    val query = MongoDBObject("$where" -> q)
    val ob = col.find(query) //> ob  : session.col.CursorType = non-empty iterator

    val json = "[%s]".format(
      ob.toList.mkString(","))
    json
  }
}
