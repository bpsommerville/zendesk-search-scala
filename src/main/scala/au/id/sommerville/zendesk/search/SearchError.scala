package au.id.sommerville.zendesk.search

import au.id.sommerville.zendesk.search.console.Entity

/**
 *
 */
sealed trait SearchError {
}


case class UnknownFieldError(field: String) extends SearchError

object NoResultsError extends SearchError