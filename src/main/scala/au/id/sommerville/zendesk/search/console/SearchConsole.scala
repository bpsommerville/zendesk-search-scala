package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.console.Command.{Help, Quit}

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

    override def readCommand: Command = Command(io.readLine)
  }


  def apply(io: ConsoleIO): ConsoleCommandResponse = {
    new CommandResponseIO(io)
  }

  val live = ConsoleCommandResponse(ConsoleIO.live)
}

class SearchConsole(
  io: ConsoleCommandResponse
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
          }
          loop()
      }
    }

    loop()
  }

}

object SearchConsole {
  def apply(io: ConsoleCommandResponse): SearchConsole = {
    new SearchConsole(io)
  }

  val live = SearchConsole(ConsoleCommandResponse.live)
}