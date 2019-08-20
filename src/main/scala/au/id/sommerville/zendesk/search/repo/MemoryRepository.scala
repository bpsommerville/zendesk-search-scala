package au.id.sommerville.zendesk.search.repo

import au.id.sommerville.zendesk.search.data.{Searchable, SearchableField, SearchableFields}
import au.id.sommerville.zendesk.search.{NoResultsError, SearchError}

import scala.collection.mutable

/**
 *
 */
class MemoryRepository[T <: Searchable](values: Seq[T])(implicit fields: SearchableFields[T]) extends SearchRepository[T] {
  val data: Map[T#IdType, T] = buildData(values)
  val indexes: Map[SearchableField[T], Map[Option[String], Seq[T]]] = buildIndexes(values)

  def buildData(values: Seq[T]): Map[T#IdType, T] = values.map(o => o._id -> o).toMap

  def buildIndexes(values: Seq[T]) = {
    val indexBuilder: mutable.Map[SearchableField[T], mutable.Map[Option[String], mutable.Buffer[T]]] = mutable.Map()

    values.foreach(v => {
      fields.foreach(f => {
        f.toSearchTerms(v).foreach(t => {
          indexBuilder.getOrElseUpdate(f, mutable.Map())
            .getOrElseUpdate(t, mutable.Buffer())
            .addOne(v)
        })
      })
    })

    indexBuilder.view.mapValues(_.view.mapValues(_.toSeq).toMap).toMap
  }

  override def get(id: T#IdType): Option[T] = {
    data.get(id)
  }

  override def search(field: String, value: Option[String]): Either[SearchError, Seq[T]] = {
    for {
      searchableField <- fields.fromString(field)
      results <- search(searchableField, value)
    } yield results
  }

  def search(field: SearchableField[T], value: Option[String]): Either[SearchError, Seq[T]] = {

    (for {
      f <- indexes.get(field)
      r <- f.get(value)
    } yield r) match {
      case Some(s) => Right(s)
      case None => Left(NoResultsError)
    }
  }
}
