package au.id.sommerville.zendesk.search.repo

import au.id.sommerville.zendesk.search.SearchError
import au.id.sommerville.zendesk.search.console.Response
import au.id.sommerville.zendesk.search.data.{Searchable, SearchableField}

/**
 *
 */
trait SearchRepository[T<: Searchable] {
  def search(field: String, value: Option[String]):Either[SearchError, Seq[T]]

}
