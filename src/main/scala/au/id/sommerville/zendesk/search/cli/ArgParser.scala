package au.id.sommerville.zendesk.search.cli

import scopt.OptionParser

class ArgParser extends OptionParser[Config]("zendesk-search") {
  head("Zendesk Search", "version 1.0")
  help("help")
    .text("prints this usage text.")
  // TODO
}

object ArgParser {

  def apply() = new ArgParser
}
