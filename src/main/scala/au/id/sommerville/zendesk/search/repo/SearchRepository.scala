package au.id.sommerville.zendesk.search.repo

import au.id.sommerville.zendesk.search.console.Response
import au.id.sommerville.zendesk.search.data.Searchable

/**
 *
 */
trait SearchRepository[T<: Searchable] {
  def search(field: String, value: String): Seq[T]

}
