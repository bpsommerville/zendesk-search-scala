package au.id.sommerville.zendesk.search

/**
 *
 */
sealed trait SearchError {
}


case class UnknownFieldError(field: String) extends SearchError

object NoResultsError extends SearchError

case class UnknownCommandError(line: String) extends SearchError

case class UnknownSubCommandError(command: String, subCommand: String) extends SearchError