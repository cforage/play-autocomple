package controllers

import com.mongodb.casbah.Imports._

import play.api._
import play.api.data.Forms._
import play.api.libs.json.JsValue
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def listProducts(id: Int) = Action {
    val json = Query.query(id)
    Ok(json).as("application/json")
  }


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
  
  def sayHello3 = Action(parse.json) { request =>
    (request.body \ "name").asOpt[String].map { name =>
      println("hello " + name)
      Ok("Hello " + name)
    }.getOrElse {
      BadRequest("Missing parameter [name]")
    }
  }
  
  def sayHello = Action(parse.json) { request =>
    println("Hello " + request.body)
    Query.update(JsonToMongo.parse(request.body))
    Ok("Hello " + request.body)
  }

  def updateProduct = Action(parse.json) { request =>
    println("Hello " + request.body)
    Query.update(JsonToMongo.parse(request.body))
    Ok("Hello " + request.body)
  }
}

case class Product(id: Int, title: String, pricing: Pricing)
case class Pricing(cost: Int, price: Int, promoPrice: Int, savings: Int, onSale: Int)


// { "id" : 9650, "pricing" : { "cost" : 1.22, "price" : 1.5, "promo_price" : 0, "savings" : 10, "on_sale" : 0 }, "title" : "Calbee Hot & Spicy Potato Chips" }
object JsonToMongo {
  def parse(product: JsValue): Map[String, Any] = {
    //product.;=
    println("parse " + product)
    Map(
        "id" -> (product \ "id").asOpt[Int],
        "price" -> (product \ "id" \ "price").asOpt[Double],
         "title" -> (product \ "title").asOpt[String]
    )
  }
}

object Query {
  def getProductCollection(): MongoCollection = {
    val mongoClient = MongoClient();
    val db = mongoClient("ctlg")
    val col = db("catalogs")
    col
  }
  
  def query(id: Int) = {
    println("query id " + id)
    val col = getProductCollection()
    val q = "/^" + id.toString + ".*/.test(this.id)"
    val query = MongoDBObject("$where" -> q)
    val ob = col.find(query)

    val json = "[%s]".format(
      ob.toList.mkString(","))
    json
  }
  
  def update(product: Map[String, Any]){
    println("update " + product)
     val col = getProductCollection()
     val query = MongoDBObject("id" -> product("id"))
     val update = $set("price" -> product("price"), 
    		 			"title" -> product("title")
    		 			)
     col.update(query, update)
  }
}
