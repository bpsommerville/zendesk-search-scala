package au.id.sommerville.zendesk.search

import java.nio.file.{Path, Paths}

import au.id.sommerville.zendesk.search.cli.{ArgParser, Config}
import au.id.sommerville.zendesk.search.console.{ConsoleCommandResponse, SearchConsole}
import au.id.sommerville.zendesk.search.data.{Loader, Organization}
import au.id.sommerville.zendesk.search.repo.MemoryRepository


object ZendeskSearch extends App {

  ArgParser().parse(args, Config()).foreach(run)


  def run( config: Config): Unit = {
    val orgs = new MemoryRepository[Organization]
    orgs.add(Loader.loadOrganizations(Paths.get("data/organizations.json")))
    SearchConsole(ConsoleCommandResponse.live, orgs).commandLoop
  }
}
