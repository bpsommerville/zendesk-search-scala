package au.id.sommerville.zendesk.search.cli

final case class Config(
  dataPath: String = "./data/",
  orgFile: String = "organizations.json",
  userFile: String = "users.json",
  ticketFile: String = "tickets.json",
  maxResultsToResolve: Int = 100
)
