package au.id.sommerville.zendesk.search.console

import java.time.OffsetDateTime

import au.id.sommerville.zendesk.search.UnitTestSpec
import au.id.sommerville.zendesk.search.data.Organization
import au.id.sommerville.zendesk.search.repo.SearchRepository


/**
 *
 */
class SearchConsoleSpec extends UnitTestSpec {


  "commandLoop" should "exit on quit" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]

    (mockConsole.printResponse _).expects(Response.Welcome)
    (mockConsole.readCommand _).expects().returns(Command.Quit)

    SearchConsole(mockConsole, mockOrgSearch).commandLoop
  }

  "commandLoop" should "output welcome message at start" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]

    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.readCommand _).expects().returns(Command.Quit)
    )

    SearchConsole(mockConsole, mockOrgSearch).commandLoop
  }

  "help" should "output help message" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]

    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.readCommand _).expects().returns(Command.Help),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Command.Quit)
    )

    SearchConsole(mockConsole, mockOrgSearch).commandLoop
  }


  "search" should "invoke search and output response" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]

    val expectedOrg = Organization(
            _id = 1234,
            url = "",
            externalId = "",
            name = "",
            domainNames = Seq(""),
            createdAt = OffsetDateTime.now(),
            details = "",
            sharedTickets = false,
            tags = Set()
          )

    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.readCommand _).expects().returns(Command.Search(Entity.Organizations, "_id", "1234")),
      (mockOrgSearch.search _).expects("_id", "1234").returns(Seq(expectedOrg)),
      (mockConsole.printResponse _).expects(Response.SearchResponse(Seq(expectedOrg))),
      (mockConsole.readCommand _).expects().returns(Command.Quit)
    )

    SearchConsole(mockConsole, mockOrgSearch).commandLoop
  }


//  "parseCommand" should "return Quit when q is input" in {
//    val mockConsole = mock[ConsoleCommandResponse]
//    mockConsole.input += "q"
//    SearchConsole(mockConsole).parseLine should equal (Command.Quit)
//  }
//
//  "parseCommand" should "return Help when h is input" in {
//    val mockConsole = mock[ConsoleCommandResponse]
//    mockConsole.input += "h"
//    SearchConsole(mockConsole).parseLine should equal (Command.Help)
//  }

}
