package au.id.sommerville.zendesk.search.data

import java.time.OffsetDateTime

trait Searchable {
  val _id: Int
}

trait SearchableFields[E <: Searchable] extends Iterable[SearchableField[E]]{
  def fromString( s: String) : SearchableField[E]
}

trait SearchableField[E <: Searchable] {
  val name: String
//  val collection: Boolean = false
  def toSearchTerm( e: E): String
}

case class SearchableStringField[E <: Searchable](
  name: String,
  get:(E) => String = (_:E) => ""
)  extends SearchableField[E] {
  override def toSearchTerm(e: E): String = get(e)
}

case class SearchableIntField[E <: Searchable](
  name: String,
  get:(E) => Int = (_:E) => 0
)  extends SearchableField[E] {
  override def toSearchTerm(e: E): String = get(e).toString
}