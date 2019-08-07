package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.UnitTestSpec

import scala.collection.mutable

/**
 *
 */
class SearchConsoleSpec extends UnitTestSpec {

  class TestConsole extends ConsoleCommandResponse {
    val commands = mutable.Queue.empty[Command]
    val output = mutable.Queue.empty[Either[Command, Response]]

    override def readCommand: Command = {
      val cmd = commands.dequeue
      output += Left(cmd)
      cmd
    }

    override def printResponse(response: Response): Unit = output += Right(response)
  }

  "commandLoop" should "exit on quit" in {
    val testConsole = new TestConsole
    testConsole.commands += Command.Quit
    SearchConsole(testConsole).commandLoop
  }

  "commandLoop" should "output welcome message at start" in {
    val testConsole = new TestConsole
    testConsole.commands += Command.Quit

    SearchConsole(testConsole).commandLoop

    testConsole.output should contain inOrder (
      Right(Response.Welcome),
      Left(Command.Quit)
    )
  }

  "help" should "output help message" in {
    val testConsole = new TestConsole
    testConsole.commands ++=  Seq(Command.Help, Command.Quit)

    SearchConsole(testConsole).commandLoop

    testConsole.output should contain theSameElementsInOrderAs List(
      Right(Response.Welcome),
      Left(Command.Help),
      Right(Response.Help),
      Left(Command.Quit)
    )
  }


//  "parseCommand" should "return Quit when q is input" in {
//    val testConsole = new TestConsole
//    testConsole.input += "q"
//    SearchConsole(testConsole).parseLine should equal (Command.Quit)
//  }
//
//  "parseCommand" should "return Help when h is input" in {
//    val testConsole = new TestConsole
//    testConsole.input += "h"
//    SearchConsole(testConsole).parseLine should equal (Command.Help)
//  }

}
