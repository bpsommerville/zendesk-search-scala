package au.id.sommerville.zendesk.search.repo

import java.time.{OffsetDateTime, ZoneOffset}
import java.util.{Locale, TimeZone}

import au.id.sommerville.zendesk.search.data.{FakeData, Organization, Searchable, SearchableFields, SearchableIntField, Ticket, User}
import au.id.sommerville.zendesk.search.{NoResultsError, UnitTestSpec, UnknownFieldError}
import faker._

import scala.util.Random


/**
 *
 */
abstract class MemoryRepositorySpec[T <: Searchable] extends UnitTestSpec {

  def generateData(count: Int): Seq[T]

  def createRepo(): MemoryRepository[T]

  def searchableFields(): SearchableFields[T]

  "add" should "add data to repository and allow them to be retrieved by _id" in {
    val repo = createRepo()
    val data = generateData(20)
    repo.add(data)
    data.foreach(e => {
      val found = repo.get(e._id)
      found.value should have(
        Symbol("_id")(e._id)
      )
    })
  }

  "search by id" should "retrieve entity with matching id" in {
    val repo = createRepo()
    val data = generateData(20)
    repo.add(data)

    val results = repo.search("_id", Some(data(4)._id.toString))
    results.right.value should have length (1)
    results.right.value should contain(data(4))
  }

  "search by id" should "return NoResultsError if nothing matches" in {
    val repo = createRepo()
    repo.add(generateData(20))

    val results = repo.search("_id", Some("412431"))
    results.left.value should equal(NoResultsError)
  }

  "search by field" should "return matching entity" in {
    val repo = createRepo()
    val data = generateData(20)
    repo.add(data)

    searchableFields.foreach(f => {
      val searchEntity = data(FakeData.randIntBetween(0, 19))
      f.toSearchTerms(searchEntity).map(
        repo.search(f.name, _).right.value should contain(searchEntity)
      )
    })

  }


  "search by field" should "return UnknownFieldError when field not found" in {
    val repo = createRepo()
    val data = generateData(20)
    repo.add(data)

    repo.search("bad", Some("anything")).left.value should equal(UnknownFieldError("bad"))

  }
}

class OrgMemoryRepositorySpec extends MemoryRepositorySpec[Organization] {
  def generateData(count: Int) = {
    (1 to count).map(FakeData.org)
  }

  def searchableFields: SearchableFields[Organization] = {
    Organization.fields
  }

  override def createRepo(): MemoryRepository[Organization] = new MemoryRepository[Organization]()
}


class UserMemoryRepositorySpec extends MemoryRepositorySpec[User] {

  def generateData(count: Int) = {
    (1 to count).map(FakeData.user)
  }

  def searchableFields: SearchableFields[User] = {
    User.fields
  }

  override def createRepo(): MemoryRepository[User] = new MemoryRepository[User]()
}

class TicketMemoryRepositorySpec extends MemoryRepositorySpec[Ticket] {

  def generateData(count: Int) = {
    (1 to count).map( _ => FakeData.ticket(FakeData.uuid()))
  }

  def searchableFields: SearchableFields[Ticket] = {
    Ticket.fields
  }

  override def createRepo(): MemoryRepository[Ticket] = new MemoryRepository[Ticket]()
}
