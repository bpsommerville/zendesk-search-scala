package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.console.Command.{Help, ListFields, Quit, Search}
import au.id.sommerville.zendesk.search.console.Entity.{Organizations, Tickets, Users}
import au.id.sommerville.zendesk.search.console.Response._
import au.id.sommerville.zendesk.search.data.{Organization, Ticket, User}
import au.id.sommerville.zendesk.search.repo.SearchRepository
import au.id.sommerville.zendesk.search.{NoResultsError, SearchError, UnknownCommandError, UnknownFieldError, UnknownSubCommandError}

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

  def readCommand: Either[SearchError, Command]
}


object ConsoleCommandResponse {

  class CommandResponseIO(
    io: ConsoleIO
  ) extends ConsoleCommandResponse {
    override def printResponse(response: Response): Unit = io.printLines(response.out)

    override def readCommand: Either[SearchError, Command] = Command.parse(io.readLine)
  }


  def apply(io: ConsoleIO): ConsoleCommandResponse = {
    new CommandResponseIO(io)
  }

  val live = ConsoleCommandResponse(ConsoleIO.live)
}

trait SearchRepos

class SearchConsole(
  io: ConsoleCommandResponse,
  orgs: SearchRepository[Organization],
  users: SearchRepository[User],
  tickets: SearchRepository[Ticket]
) {


  def commandLoop = {
    io.printResponse(Response.Welcome)

    @tailrec
    def loop(): Unit = {
      io.readCommand match {
        case Right(Quit) =>
        case cmdOrErr =>
          cmdOrErr match {
            case Right(cmd) =>
              cmd match {
                case Help => io.printResponse(Response.Help)
                case Search(e, f, v) => io.printResponse(search(e, f, v))
                case ListFields(e) => io.printResponse(fields(e))
              }
            case Left(err) =>
              err match {
                case UnknownCommandError(l) => io.printResponse(UnknownCommandResponse(l))
                case UnknownSubCommandError(c,s) => io.printResponse(UnknownSubCommandResponse(c, s))
                case other => io.printResponse(UnexpectedErrorResponse(other))
              }
          }
          loop()
      }
    }

    loop()
  }

  def search(entity: Entity, field: String, value: String): SearchResponse = {
    val (f, v) = if  (field.startsWith("!"))  (field.substring(1), None) else (field, Some(value))
    (entity match {
      case Organizations => orgs.search(f, v)
      case Users => users.search(f, v)
      case Tickets => tickets.search(f, v)
    }) match {
      case Right(r) => SuccessfulSearchResponse(r)
      case Left(e) => e match {
        case NoResultsError => NotFoundSearchResponse(entity, f, v)
        case e: UnknownFieldError => UnknownFieldSearchResponse(entity, e.field)
        case other => UnexpectedErrorResponse(other)
      }
    }
  }

  def fields(entity: Entity): Response = {
    entity match {
      case Organizations => EntityFields[Organization](entity)
      case Users => EntityFields[User](entity)
      case Tickets => EntityFields[Ticket](entity)
    }
  }

}

object SearchConsole {
  def apply(io: ConsoleCommandResponse, orgs: SearchRepository[Organization],
    users: SearchRepository[User], tickets: SearchRepository[Ticket]): SearchConsole = {
    new SearchConsole(io, orgs, users, tickets)
  }

}