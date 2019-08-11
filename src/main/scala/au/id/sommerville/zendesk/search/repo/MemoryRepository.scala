package au.id.sommerville.zendesk.search.repo

import au.id.sommerville.zendesk.search.data.{Organization, Searchable}

/**
 *
 */
class MemoryRepository[T<: Searchable] extends SearchRepository[T] {
  var data: Map[Int, Seq[T]] = Map()

  def find(id: Int): Seq[T] = {
    data(id)
  }

  def add(values: Seq[T]): Any = {
    data = values.map(o => o._id -> Seq(o)).toMap
  }

  override def search(field: String, value: String): Seq[T] = {
    field match {
      case "_id" => find( value.toInt)
    }
  }
}
