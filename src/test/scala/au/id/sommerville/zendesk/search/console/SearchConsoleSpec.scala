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
      (mockOrgSearch.search _).expects("_id", "1234").returns(Some(Seq(expectedOrg))),
      (mockConsole.printResponse _).expects(Response.SuccessfulSearchResponse(Seq(expectedOrg))),
      (mockConsole.readCommand _).expects().returns(Command.Quit)
    )

    SearchConsole(mockConsole, mockOrgSearch).commandLoop
  }

  "search" should "display message when nothing found" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]

     inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.readCommand _).expects().returns(Command.Search(Entity.Organizations, "_id", "1234")),
      (mockOrgSearch.search _).expects("_id", "1234").returns(None),
      (mockConsole.printResponse _).expects(Response.NotFoundSearchResponse(Entity.Organizations, "_id", "1234")),
      (mockConsole.readCommand _).expects().returns(Command.Quit)
    )

    SearchConsole(mockConsole, mockOrgSearch).commandLoop
  }

  "fields" should "list fields for organization" in {
      val mockConsole = mock[ConsoleCommandResponse]
      val mockOrgSearch = mock[SearchRepository[Organization]]
      inSequence(
        (mockConsole.printResponse _).expects(Response.Welcome),
        (mockConsole.readCommand _).expects().returns(Command.ListFields(Entity.Organizations)),
        (mockConsole.printResponse _).expects(Response.EntityFields(Entity.Organizations, Organization.fields)),
        (mockConsole.readCommand _).expects().returns(Command.Quit)
      )

      SearchConsole(mockConsole, mockOrgSearch).commandLoop
    }
}
