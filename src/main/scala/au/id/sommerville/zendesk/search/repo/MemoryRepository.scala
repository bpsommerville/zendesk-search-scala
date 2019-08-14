package au.id.sommerville.zendesk.search.repo

import au.id.sommerville.zendesk.search.console.Entity.Organizations
import au.id.sommerville.zendesk.search.data.{Organization, Searchable, SearchableField, SearchableFields, ZendeskPickle}

import scala.collection.mutable

/**
 *
 */
class MemoryRepository[T <: Searchable](implicit fields: SearchableFields[T] )extends SearchRepository[T] {
  var data: Map[Int, T] = Map()
//  var indexes: mutable.Map[SearchableField[T], mutable.Map[String, mutable.Seq[T]]] = mutable.Map()

  def find(id: Int): Seq[T] = {
    Seq(data(id))
  }

  def add(values: Seq[T]): Any = {
    data = values.map(o => o._id -> o).toMap

//    values.foreach(v => {
//      val value = ZendeskPickle.writeJs(v)
//      Organization.fields.foreach(f => {
//        indexes.getOrElseUpdate(f, mutable.Map())
//          .getOrElseUpdate(value(f.name).str, mutable.Seq())
//          .appended(v)
//      })
//    })
  }

  override def search(field: String, value: String): Option[Seq[T]] = {
    val searchableField = fields.fromString(field)
    Option(data.values.filter(( e) => searchableField.toSearchTerm(e) == value).toSeq).filter(_.nonEmpty)
  }
}
