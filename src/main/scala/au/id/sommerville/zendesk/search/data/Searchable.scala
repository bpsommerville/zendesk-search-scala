package au.id.sommerville.zendesk.search.data

import java.time.OffsetDateTime

import au.id.sommerville.zendesk.search.SearchError
import au.id.sommerville.zendesk.search.console.Entity

trait Searchable {
  val _id: Int
}

trait SearchableFields[E <: Searchable] extends Iterable[SearchableField[E]]{
  def fromString( s: String) : Either[SearchError, SearchableField[E]]
  val entity: Entity
}

trait SearchableField[E <: Searchable] {
  val name: String
//  val collection: Boolean = false
  def toSearchTerms( e: E): Iterable[String]
}

trait SearchableValueField[E <: Searchable, T] extends SearchableField[E] {
  val get:(E) => T
  override def toSearchTerms(e: E): Iterable[String] = Seq(get(e).toString)
}

trait SearchableCollectionField[E <: Searchable, T] extends SearchableField[E] {
  val get:(E) => Iterable[T]
  override def toSearchTerms(e: E): Iterable[String] = get(e).map(_.toString)
}

case class SearchableStringField[E <: Searchable](
  name: String,
  get:(E) => String
)  extends SearchableValueField[E, String]

case class SearchableIntField[E <: Searchable](
  name: String,
  get:(E) => Int
)  extends SearchableValueField[E, Int]

case class SearchableBoolField[E <: Searchable](
  name: String,
  get:(E) => Boolean
)  extends SearchableValueField[E, Boolean]

case class SearchableDateTimeField[E <: Searchable](
  name: String,
  get:(E) => OffsetDateTime
)  extends SearchableValueField[E, OffsetDateTime]

case class SearchableStringCollectionField[E <: Searchable](
  name: String,
  get:(E) => Iterable[String]
)  extends SearchableCollectionField[E, String]
