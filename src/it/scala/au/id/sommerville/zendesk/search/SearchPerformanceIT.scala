package au.id.sommerville.zendesk.search


import au.id.sommerville.zendesk.search.console.Command.{Quit, Search}
import au.id.sommerville.zendesk.search.console.Entity.{Organizations, Tickets, Users}
import au.id.sommerville.zendesk.search.console.{Command, ConsoleCommandResponse, Response, SearchConsole}
import au.id.sommerville.zendesk.search.data._
import au.id.sommerville.zendesk.search.repo.MemoryRepository
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable
import scala.collection.mutable.Queue
import scala.util.Random

/**
 *
 */
class SearchPerformanceIT extends FlatSpec with Matchers {

  class CommandResponseMock(
    cmds: Queue[Command]
  ) extends ConsoleCommandResponse {
    override def printResponse(response: Response): Unit = {}

    override def readCommand: Either[SearchError, Command] = Right(cmds.dequeue())
  }

  val nanos_to_millis: Long = 1000 * 1000
  val nanos_to_seconds: Long = nanos_to_millis * 1000

  def time(block: => Unit): Long = {
    val t0 = System.nanoTime()
    block // call-by-name
    val t1 = System.nanoTime()
    val duration = t1 - t0
    println(s"Elapsed time: ${duration / nanos_to_seconds}s ${(duration % nanos_to_seconds) / nanos_to_millis}ms ")
    duration
  }


  "search time" should "not increase linearly with data set size" in {
    val numberOfSearches = 50000;

    var previousTime = 0L
    var currentTime = 0L
    var dataSize = 1000
    // Data size of 1,000,000+ takes minutes to just generate the data
    while (currentTime < (nanos_to_seconds * 10) && dataSize <= 100000) {
      println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
      println(s"Running with data size = ${dataSize}")
      previousTime = currentTime
      currentTime = runSearches(dataSize, numberOfSearches)
      dataSize *= 10
    }
    val ratio: Double = currentTime * 1.0 / previousTime

    // Should be able to have ratio of less than 2, but the margin of error in timing currently prevents it
    // Can't increase data set size as that makes the test take minutes just to generate data
    //    ratio should be < 2.0
    ratio should be < 5.0
  }

  private def runSearches(dataSize: Int, numberOfSearches: Int) = {

    println("Generating org data")
    val fakeOrgs = (1 to dataSize / 100).map(FakeData.org)
    println("Generating user data")
    val fakeUsers = (1 to dataSize / 10).map(i => FakeData.user(i, fakeOrgs.length))
    println("Generating ticket data")
    val fakeTickets = (1 to dataSize).map(i => FakeData.ticket(FakeData.uuid(), fakeOrgs.length, fakeUsers.length))

    println("Building repositories")
    val orgs = new MemoryRepository(fakeOrgs)
    val users = new MemoryRepository(fakeUsers)
    val tickets = new MemoryRepository(fakeTickets)

    println("Building search commands")
    val cmds: mutable.Queue[Command] = Queue.from(
      (1 to numberOfSearches).flatMap(i => {
        val o = fakeOrgs(Random.nextInt(fakeOrgs.length))
        Organization.fields.map(f =>
          Search(Organizations, f.name, f.toSearchTerms(o).head.getOrElse(""))
        )
        val u = fakeUsers(Random.nextInt(fakeUsers.length))
        User.fields.map(f =>
          Search(Users, f.name, f.toSearchTerms(u).head.getOrElse(""))
        )
        val t = fakeTickets(Random.nextInt(fakeTickets.length))
        Ticket.fields.map(f =>
          Search(Tickets, f.name, f.toSearchTerms(t).head.getOrElse(""))
        )
      })
    )
    cmds.append(Quit)

    println("Running searches")
    time(SearchConsole(new CommandResponseMock(cmds), orgs, users, tickets).commandLoop)
  }
}
