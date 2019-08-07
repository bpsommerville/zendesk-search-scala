package au.id.sommerville.zendesk.search

import au.id.sommerville.zendesk.search.cli.{ArgParser, Config}
import au.id.sommerville.zendesk.search.console.SearchConsole


object ZendeskSearch extends App {

  ArgParser().parse(args, Config()).foreach(run)

  def run( config: Config): Unit = {
    SearchConsole.live.commandLoop
  }
}
