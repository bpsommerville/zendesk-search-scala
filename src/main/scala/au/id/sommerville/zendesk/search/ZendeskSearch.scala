package au.id.sommerville.zendesk.search

import java.nio.file.{Path, Paths}

import au.id.sommerville.zendesk.search.cli.{ArgParser, Config}
import au.id.sommerville.zendesk.search.console.{ConsoleCommandResponse, SearchConsole}
import au.id.sommerville.zendesk.search.data.{Loader, Organization, Ticket, User}
import au.id.sommerville.zendesk.search.repo.MemoryRepository


object ZendeskSearch extends App {

  ArgParser().parse(args, Config()).foreach(run)


  def run( config: Config): Unit = {
    val orgs = new MemoryRepository[Organization]
    orgs.add(Loader.loadOrganizations(Paths.get("data/organizations.json")))
    val users = new MemoryRepository[User]
    users.add(Loader.loadUsers(Paths.get("data/users.json")))
    val tickets = new MemoryRepository[Ticket]
    tickets.add(Loader.loadTickets(Paths.get("data/tickets.json")))

    SearchConsole(ConsoleCommandResponse.live, orgs, users, tickets).commandLoop
  }
}
