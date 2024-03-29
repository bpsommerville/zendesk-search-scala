package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search._
import au.id.sommerville.zendesk.search.console.Entity.Organizations
import au.id.sommerville.zendesk.search.data._
import au.id.sommerville.zendesk.search.repo.SearchRepository


/**
 *
 */
class SearchConsoleSpec extends UnitTestSpec {


  "commandLoop" should "exit on quit" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]

    (mockConsole.printResponse _).expects(Response.Welcome)
    (mockConsole.printResponse _).expects(Response.Help)
    (mockConsole.readCommand _).expects().returns(Right(Command.Quit))

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }

  "commandLoop" should "output welcome message at start" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]

    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }

  "help" should "output help message" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]

    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Right(Command.Help)),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }


  "search organizations" should "invoke search and output response" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]

    val expectedOrg = FakeData.org(1234)

    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Right(Command.Search(Entity.Organizations, "_id", "1234"))),
      (mockOrgSearch.search _).expects("_id", Some("1234")).returns(Right(Seq(expectedOrg))),
      (mockUserSearch.search _).expects("organization_id", Some("1234")).returns(Left(NoResultsError)),
      (mockTicketSearch.search _).expects("organization_id", Some("1234")).returns(Left(NoResultsError)),
      (mockConsole.printResponse _).expects(Response.SuccessfulSearchResponse(Seq(ResolvedOrganization(expectedOrg)))),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }

  "search users" should "invoke search and output response" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]

    val expectedUser = FakeData.user(1234).copy(organizationId = None)

    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Right(Command.Search(Entity.Users, "_id", "1234"))),
      (mockUserSearch.search _).expects("_id", Some("1234")).returns(Right(Seq(expectedUser))),
      (mockTicketSearch.search _).expects("submitter_id", Some("1234")).returns(Left(NoResultsError)),
      (mockTicketSearch.search _).expects("assignee_id", Some("1234")).returns(Left(NoResultsError)),
      (mockConsole.printResponse _).expects(Response.SuccessfulSearchResponse(Seq(ResolvedUser(expectedUser)))),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }

  "search tickets" should "invoke search and output response" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]

    val expectedTicket = FakeData.ticket("12345").copy(organizationId = None, submitterId = 23, assigneeId = None)

    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Right(Command.Search(Entity.Tickets, "_id", "1234"))),
      (mockTicketSearch.search _).expects("_id", Some("1234")).returns(Right(Seq(expectedTicket))),
      (mockUserSearch.get _).expects(23).returns(None),
      (mockConsole.printResponse _).expects(Response.SuccessfulSearchResponse(Seq(ResolvedTicket(expectedTicket)))),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }

  "search for !field" should "invoke search with None value and output response" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]

    val expectedTicket = FakeData.ticket("12345").copy(organizationId = None, submitterId = 23, assigneeId = None)

    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Right(Command.Search(Entity.Tickets, "!description", ""))),
      (mockTicketSearch.search _).expects("description", None).returns(Right(Seq(expectedTicket))),
      (mockUserSearch.get _).expects(23).returns(None),
      (mockConsole.printResponse _).expects(Response.SuccessfulSearchResponse(Seq(ResolvedTicket(expectedTicket)))),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }

  "search" should "display message when nothing found" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]

    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Right(Command.Search(Entity.Organizations, "_id", "1234"))),
      (mockOrgSearch.search _).expects("_id", Some("1234")).returns(Left(NoResultsError)),
      (mockConsole.printResponse _).expects(Response.NotFoundSearchResponse(Organizations, "_id", Some("1234"))),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }
  "search" should "display error when field is not valid" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]

    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Right(Command.Search(Entity.Organizations, "foo", "1234"))),
      (mockOrgSearch.search _).expects("foo", Some("1234")).returns(Left(UnknownFieldError("foo"))),
      (mockConsole.printResponse _).expects(Response.UnknownFieldSearchResponse(Organizations, "foo")),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }

  "fields" should "list fields for organization" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]
    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Right(Command.ListFields(Entity.Organizations))),
      (mockConsole.printResponse _).expects(Response.EntityFields[Organization](Entity.Organizations)),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }
  "fields" should "list fields for users" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]
    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Right(Command.ListFields(Entity.Users))),
      (mockConsole.printResponse _).expects(Response.EntityFields[User](Entity.Users)),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }
  "fields" should "list fields for tickets" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]
    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Right(Command.ListFields(Entity.Tickets))),
      (mockConsole.printResponse _).expects(Response.EntityFields[Ticket](Entity.Tickets)),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }

  "unknown command" should "give error message" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]
    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Left(UnknownCommandError("foo"))),
      (mockConsole.printResponse _).expects(Response.UnknownCommandResponse("foo")),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }
  "unknown subcommand" should "give error message" in {
    val mockConsole = mock[ConsoleCommandResponse]
    val mockOrgSearch = mock[SearchRepository[Organization]]
    val mockUserSearch = mock[SearchRepository[User]]
    val mockTicketSearch = mock[SearchRepository[Ticket]]
    inSequence(
      (mockConsole.printResponse _).expects(Response.Welcome),
      (mockConsole.printResponse _).expects(Response.Help),
      (mockConsole.readCommand _).expects().returns(Left(UnknownSubCommandError("foo", "blah"))),
      (mockConsole.printResponse _).expects(Response.UnknownSubCommandResponse("foo", "blah")),
      (mockConsole.readCommand _).expects().returns(Right(Command.Quit))
    )

    SearchConsole(mockConsole, mockOrgSearch, mockUserSearch, mockTicketSearch).commandLoop
  }
}
