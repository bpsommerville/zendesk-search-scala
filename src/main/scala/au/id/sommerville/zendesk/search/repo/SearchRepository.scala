package au.id.sommerville.zendesk.search.repo

import au.id.sommerville.zendesk.search.SearchError
import au.id.sommerville.zendesk.search.data.Searchable

/**
 *
 */
trait SearchRepository[T <: Searchable] {
  def search(field: String, value: Option[String]): Either[SearchError, Seq[T]]

  def get(id: T#IdType): Option[T]
}
