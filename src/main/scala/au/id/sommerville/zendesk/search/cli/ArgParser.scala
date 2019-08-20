package au.id.sommerville.zendesk.search.cli

import scopt.OptionParser

class ArgParser extends OptionParser[Config]("zendesk-search") {
  head("Zendesk Search", "version 1.0")
  opt[Int]('m', "maxResultsToResolve")
        .action((x, c) => c.copy(maxResultsToResolve = x))
        .text("Maximum number of results for which to resolve related entities" )
  opt[String]('d', "data")
        .action((x, c) => c.copy(dataPath = x))
        .text("Path to folder where data will be loaded from" )
  opt[String]('o', "organizations")
        .action((x, c) => c.copy(orgFile = x))
        .text("Name of file in data folder that organization data will be loaded from" )
  opt[String]('u', "users")
        .action((x, c) => c.copy(userFile = x))
        .text("Name of file in data folder that user data will be loaded from" )
  opt[String]('t', "tickets")
        .action((x, c) => c.copy(ticketFile = x))
        .text("Name of file in data folder that ticket data will be loaded from" )

  help("help")
}

object ArgParser {

  def apply() = new ArgParser
}
