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

  def getProducts(id: Int) = Action {
    val json = Query.query(id)
    Ok(json).as("application/json")
  }

  def updateProduct = Action(parse.json) { request =>
    println("Hello " + request.body)
    Query.update(request.body)
    Ok("Hello " + request.body)
  }
}

object Query {
  def getProductCollection(): MongoCollection = {
    val mongoClient = MongoClient();
    val db = mongoClient(Play.current.configuration.getString("mongo.database").get)
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

  def update(productJson: JsValue) {
    val product = parseJson(productJson)
    println("update " + product)
    val col = getProductCollection()
    val query = MongoDBObject("id" -> product("id"))
    val update = $set("pricing.price" -> product("price"),
      "title" -> product("title"))
    col.update(query, update)
  }

  def parseJson(product: JsValue): Map[String, Any] = {
    //product.;=
    println("parse " + product)
    Map(
      "id" -> (product \ "id").asOpt[Int],
      "price" -> (product \ "price").asOpt[Double],
      "title" -> (product \ "title").asOpt[String])
  }
}
