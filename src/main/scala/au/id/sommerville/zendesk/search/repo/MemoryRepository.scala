package au.id.sommerville.zendesk.search.repo

import au.id.sommerville.zendesk.search.data.{Searchable, SearchableField, SearchableFields}
import au.id.sommerville.zendesk.search.{NoResultsError, SearchError}

/**
 *
 */
class MemoryRepository[T <: Searchable](implicit fields: SearchableFields[T]) extends SearchRepository[T] {
  var data: Map[T#IdType, T] = Map()
  //  var indexes: mutable.Map[SearchableField[T], mutable.Map[String, mutable.Seq[T]]] = mutable.Map()

  override def get(id: T#IdType): Option[T] = {
    data.get(id)
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

  override def search(field: String, value: Option[String]): Either[SearchError, Seq[T]] = {
    for {
      searchableField <- fields.fromString(field)
      results <- search(searchableField, value)
    } yield results
  }

  def search(field: SearchableField[T], value: Option[String]): Either[SearchError, Seq[T]] = {

    def entityMatchesSearch(e: T ) = {
      field.toSearchTerms(e).exists(_ == value)
    }

    data.values.filter(entityMatchesSearch).toSeq match {
      case Nil => Left(NoResultsError)
      case r => Right(r)
    }
  }
}
