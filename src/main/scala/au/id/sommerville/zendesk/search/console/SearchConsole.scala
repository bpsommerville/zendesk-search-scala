package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.console.Command.{Help, ListFields, Quit, Search}
import au.id.sommerville.zendesk.search.console.Entity.Organizations
import au.id.sommerville.zendesk.search.console.Response.{EntityFields, NotFoundSearchResponse, SearchResponse, SuccessfulSearchResponse}
import au.id.sommerville.zendesk.search.data.{Organization, Searchable}
import au.id.sommerville.zendesk.search.repo.SearchRepository

import scala.annotation.tailrec
import scala.io.StdIn


trait ConsoleIO {
  def printLn(line: String): Unit

  def printLines(lines: Seq[String]): Unit = {
    lines.foreach(printLn)
  }

  def readLine: String
}

object ConsoleIO {

  object live extends ConsoleIO {
    override def printLn(line: String): Unit = Console.println(line)

    override def readLine: String = StdIn.readLine(">")
  }

}


trait ConsoleCommandResponse {

  def printResponse(response: Response): Unit

  def readCommand: Command
}


object ConsoleCommandResponse {

  class CommandResponseIO(
    io: ConsoleIO
  ) extends ConsoleCommandResponse {
    override def printResponse(response: Response): Unit = io.printLines(response.out)

    override def readCommand: Command = Command.parse(io.readLine)
  }


  def apply(io: ConsoleIO): ConsoleCommandResponse = {
    new CommandResponseIO(io)
  }

  val live = ConsoleCommandResponse(ConsoleIO.live)
}

trait SearchRepos

class SearchConsole(
  io: ConsoleCommandResponse,
  orgs: SearchRepository[Organization]
) {


  def commandLoop = {
    io.printResponse(Response.Welcome)

    @tailrec
    def loop(): Unit = {
      io.readCommand match {
        case Quit =>
        case cmd =>
          cmd match {
            case Help => io.printResponse(Response.Help)
            case Search(e, f, v) => io.printResponse(search(e, f, v))
            case ListFields(e) => io.printResponse(fields(e))
          }
          loop()
      }
    }

    loop()
  }

  def search(entity: Entity, field: String, value: String): SearchResponse = {
    (entity match {
      case Organizations => orgs.search(field, value)
    }) map {
      SuccessfulSearchResponse(_)
    } getOrElse(
      NotFoundSearchResponse(entity, field, value)
    )
  }

  def fields(entity: Entity): Response = {
    entity match {
      case Organizations => EntityFields(entity, Organization.fields)
    }
  }

}

object SearchConsole {
  def apply(io: ConsoleCommandResponse, orgs: SearchRepository[Organization]): SearchConsole = {
    new SearchConsole(io, orgs)
  }

}