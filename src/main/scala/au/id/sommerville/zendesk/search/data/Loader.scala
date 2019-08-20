package au.id.sommerville.zendesk.search.data

import java.nio.file.Path


/**
 *
 */
object Loader {
  def loadOrganizations(inputPath: Path): Seq[Organization] = {
    ZendeskPickle.read[Seq[Organization]](inputPath)
  }

  def loadUsers(inputPath: Path): Seq[User] = {
    ZendeskPickle.read[Seq[User]](inputPath)
  }

  def loadTickets(inputPath: Path): Seq[Ticket] = {
    ZendeskPickle.read[Seq[Ticket]](inputPath)
  }

}
