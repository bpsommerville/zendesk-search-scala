package au.id.sommerville.zendesk.search.data

import java.time.OffsetDateTime

import au.id.sommerville.zendesk.search.SearchError
import au.id.sommerville.zendesk.search.console.Entity

trait Searchable {
  type IdType
  val _id: IdType
}

trait SearchableFields[E <: Searchable] extends Iterable[SearchableField[E]]{
  def fromString( s: String) : Either[SearchError, SearchableField[E]]
  val entity: Entity
}

trait SearchableField[E <: Searchable] {
  val name: String
  def toSearchTerms( e: E): Iterable[Option[String]]
}

trait SearchableValueField[E <: Searchable, T] extends SearchableField[E] {
  val get:(E) => Option[T]
  override def toSearchTerms(e: E): Iterable[Option[String]] = Seq(get(e).map(_.toString))
}

trait SearchableCollectionField[E <: Searchable, T] extends SearchableField[E] {
  val get:(E) => Option[Iterable[T]]
  override def toSearchTerms(e: E):  Iterable[Option[String]]= {
    get(e).flatMap(
      Some(_).filter(_.nonEmpty).map(
        _.map(v => Some(v.toString))
      )
    ).getOrElse(Seq(None))
  }
}

case class SearchableStringField[E <: Searchable](
  name: String,
  get:(E) => Option[String]
)  extends SearchableValueField[E, String]

case class SearchableIntField[E <: Searchable](
  name: String,
  get:(E) => Option[Int]
)  extends SearchableValueField[E, Int]

case class SearchableBoolField[E <: Searchable](
  name: String,
  get:(E) => Option[Boolean]
)  extends SearchableValueField[E, Boolean]

case class SearchableDateTimeField[E <: Searchable](
  name: String,
  get:(E) => Option[OffsetDateTime]
)  extends SearchableValueField[E, OffsetDateTime]

case class SearchableStringCollectionField[E <: Searchable](
  name: String,
  get:(E) => Option[Iterable[String]]
)  extends SearchableCollectionField[E, String]
