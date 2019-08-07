package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.UnitTestSpec
import au.id.sommerville.zendesk.search.console.{Command, ConsoleCommandResponse, ConsoleIO, Response}
import org.scalatest.prop.TableDrivenPropertyChecks

import scala.collection.mutable

class ConsoleCommandResponseSpec extends UnitTestSpec with TableDrivenPropertyChecks {

  class TestConsole extends ConsoleIO {
    val input = mutable.Queue.empty[String]
    val output = mutable.Queue.empty[String]

    override def printLn(line: String): Unit = output += line

    override def readLine: String = input.dequeue()
  }

  "printResponse" should "print the lines from the response to the console" in {
    val testConsole = new TestConsole
    val ccr = ConsoleCommandResponse(testConsole)

    ccr.printResponse(new Response {
      def out = Seq("one", "two", "three")
    })

    testConsole.output should contain theSameElementsInOrderAs (
      Seq("one", "two", "three")
      )
  }

  val commands = Table(
    ("line", "command"),
    ("h", Command.Help),
    ("help", Command.Help),
    ("q", Command.Quit),
    ("quit", Command.Quit),
  )
  "readCommand" should "read a line and convert it to the appropriate command" in {
    val testConsole = new TestConsole
    val ccr = ConsoleCommandResponse(testConsole)

    forAll(commands) {
      (line: String, cmd: Command) => {
        testConsole.input += line
        ccr.readCommand should equal(cmd)
      }
    }
  }

  val unknown = Table(
      ("line"),
      ("he"),
      ("1231p"),
      ("sddd"),
      (""),
    )
  "readCommand" should "return Unknown if line doesn't match any commands" in {
    val testConsole = new TestConsole
    val ccr = ConsoleCommandResponse(testConsole)

    forAll(unknown) {
      (line: String) => {
        testConsole.input += line
        ccr.readCommand should equal(Command.Unknown)
      }
    }
  }
}
