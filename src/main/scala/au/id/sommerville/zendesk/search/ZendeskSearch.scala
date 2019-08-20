package au.id.sommerville.zendesk.search

import java.nio.file.Paths

import au.id.sommerville.zendesk.search.cli.{ArgParser, Config}
import au.id.sommerville.zendesk.search.console.{ConsoleCommandResponse, SearchConsole}
import au.id.sommerville.zendesk.search.data.Loader
import au.id.sommerville.zendesk.search.repo.MemoryRepository


object ZendeskSearch extends App {

  ArgParser().parse(args, Config()) match {
    case Some(config) =>
      run(config)
    case None =>
    // arguments are bad, error message will have been displayed
  }

  def run(config: Config): Unit = {
    val orgs = new MemoryRepository(Loader.loadOrganizations(Paths.get(config.dataPath, config.orgFile)))
    val users = new MemoryRepository(Loader.loadUsers(Paths.get(config.dataPath, config.userFile)))
    val tickets = new MemoryRepository(Loader.loadTickets(Paths.get(config.dataPath, config.ticketFile)))

    SearchConsole(ConsoleCommandResponse.live, orgs, users, tickets, config.maxResultsToResolve).commandLoop
  }
}
