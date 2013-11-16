package controllers

import com.mongodb.casbah.Imports._

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def listProduct(id: Int) = Action {
    val json = Query.query(id)
	Ok(json).as("application/json")
  }

}

object Query {
  def query(id: Int) = {
    println("query id " + id)
    val mongoClient = MongoClient(); 
    val db = mongoClient("ctlg")
    db.collectionNames
    val col = db("catalogs")
    val q = "/^" + id.toString + ".*/.test(this.id)"
    val query = MongoDBObject("$where" -> q)
    val ob = col.find(query) //> ob  : session.col.CursorType = non-empty iterator
    
    val json = "[%s]".format(
	  ob.toList.mkString(",")
	)
	json
  }
}