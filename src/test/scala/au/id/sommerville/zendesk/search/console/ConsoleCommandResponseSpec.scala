package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.UnitTestSpec
import org.scalatest.prop.TableDrivenPropertyChecks


class ConsoleCommandResponseSpec extends UnitTestSpec with TableDrivenPropertyChecks {

  "printResponse" should "print the lines from the response to the console" in {
    val mockConsoleIO = mock[ConsoleIO]

    val ccr = ConsoleCommandResponse(mockConsoleIO)

    (mockConsoleIO.printLines _).expects(Seq("one", "two", "three"));

    ccr.printResponse(new Response {
      def out = Seq("one", "two", "three")
    })
  }

  val commands = Table(
    ("line", "command"),
    ("h", Command.Help),
    ("help", Command.Help),
    ("q", Command.Quit),
    ("quit", Command.Quit),
    ("search organization _id 1234", Command.Search(Entity.Organizations, "_id", "1234")),
    ("s o _id 1234", Command.Search(Entity.Organizations, "_id", "1234")),
    ("s org aa one two three", Command.Search(Entity.Organizations, "aa", "one two three")),
    ("search user _id 1234", Command.Search(Entity.Users, "_id", "1234")),
    ("search u _id 1234", Command.Search(Entity.Users, "_id", "1234")),
    ("search ticket _id 1234", Command.Search(Entity.Tickets, "_id", "1234")),
    ("search t _id 1234", Command.Search(Entity.Tickets, "_id", "1234")),
    ("fields organization", Command.ListFields(Entity.Organizations)),
    ("f org", Command.ListFields(Entity.Organizations)),
    ("f o", Command.ListFields(Entity.Organizations)),
    ("f user", Command.ListFields(Entity.Users)),
    ("f u", Command.ListFields(Entity.Users)),
    ("f ticket", Command.ListFields(Entity.Tickets)),
    ("f t", Command.ListFields(Entity.Tickets)),
  )
  "readCommand" should "read a line and convert it to the appropriate command" in {
    val mockConsoleIO = stub[ConsoleIO]
    val ccr = ConsoleCommandResponse(mockConsoleIO)

    forAll(commands) {
      (line: String, cmd: Command) => {
        (mockConsoleIO.readLine _).when().once().returns(line)
        ccr.readCommand should equal(cmd)
      }
    }
  }

  val unknown = Table(
    ("line"),
    ("he"),
    ("1231p"),
    ("sddd"),
    ("s foo _id 1234"),
    (""),
  )
  "readCommand" should "return Unknown if line doesn't match any commands" in {
    val mockConsoleIO = stub[ConsoleIO]
    val ccr = ConsoleCommandResponse(mockConsoleIO)

    forAll(unknown) {
      (line: String) => {

        (mockConsoleIO.readLine _).when().once().returns(line)
        ccr.readCommand should equal(Command.Unknown)
      }
    }
  }
}
