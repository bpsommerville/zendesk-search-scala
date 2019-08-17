package au.id.sommerville.zendesk.search.data

import java.time.{OffsetDateTime, ZoneOffset}
import java.util.{Locale, TimeZone}

import faker.{Internet, Lorem, Name, PhoneNumber}

import scala.util.Random

/**
 *
 */
object FakeData {
  val timezones = TimeZone.getAvailableIDs

  val locales = Locale.getAvailableLocales.map(_.toLanguageTag)


  def genSeq(max: Int, gen: () => String) = {
    for (i <- 0 to Random.nextInt(max)) yield gen()
  }

  def randIntBetween(low: Int, high: Int): Int = {
    Random.nextInt(high - low + 1) + low
  }

  def dateBetween(startYear: Int, endYear: Int): OffsetDateTime = {
    OffsetDateTime.of(
      randIntBetween(startYear, endYear),
      randIntBetween(1, 12),
      randIntBetween(1, 28),
      randIntBetween(0, 23),
      randIntBetween(0, 59),
      randIntBetween(0, 59),
      0,
      ZoneOffset.ofHours(randIntBetween(-11, 11))
    )
  }

  def maybe[FT](genValue: () => FT): Option[FT] = {
    if (Random.nextInt(100) < 30) None else Some(genValue())
  }


  def uuid(): String = java.util.UUID.randomUUID.toString

  def org(id: Int): Organization = {
    Organization(
      _id = id,
      url = s"http://initech.zendesk.com/api/v2/organizations/${id}.json",
      externalId = uuid(),
      name = Name.name,
      domainNames = genSeq(4, () => Internet.domain_name),
      createdAt = dateBetween(1983, 2019),
      details = Lorem.paragraph(),
      sharedTickets = Random.nextBoolean(),
      tags = Lorem.words(Random.nextInt(4)).toSet
    )
  }

  def user(id: Int): User = {
    User(
      _id = id,
      url = s"http://initech.zendesk.com/api/v2/users/${id}.json",
      externalId = uuid(),
      name = Name.name,
      alias = maybe(() => Name.name),
      createdAt = dateBetween(1983, 2019),
      active = Random.nextBoolean(),
      verified = maybe(Random.nextBoolean),
      shared = Random.nextBoolean(),
      locale = maybe(() => locales(Random.nextInt(locales.length))),
      timezone = maybe(() => timezones(Random.nextInt(timezones.length))),
      lastLoginAt = dateBetween(2001, 2019),
      email = maybe(() => Internet.email),
      phone = PhoneNumber.phone_number,
      signature = Lorem.sentence(Random.nextInt(6)),
      organizationId = maybe(() => Random.nextInt(20)),
      tags = Lorem.words(Random.nextInt(4)).toSet,
      suspended = Random.nextBoolean(),
      role = Seq("agent", "admin", "end-user")(Random.nextInt(3))
    )
  }

  def ticket(id : String): Ticket = {
    Ticket(
      _id = id,
      url = s"http=//initech.zendesk.com/api/v2/tickets/${id}.json",
      externalId = uuid,
      createdAt = dateBetween(1983, 2019),
      `type` = maybe(() => Seq("incident", "option", "problem", "task")(Random.nextInt(4))),
      subject = Lorem.sentence(Random.nextInt(15) + 2),
      description = maybe(() => Lorem.paragraph(Random.nextInt(5))),
      priority = Seq("low", "normal", "high", "urgent")(Random.nextInt(4)),
      status = Seq("open", "pending", "hold", "solved", "closed")(Random.nextInt(5)),
      submitterId = Random.nextInt(20),
      assigneeId = maybe(() => Random.nextInt(20)),
      organizationId = maybe(() => Random.nextInt(20)),
      tags = Lorem.words(Random.nextInt(4)).toSet,
      hasIncidents = Random.nextBoolean(),
      dueAt = maybe(() => dateBetween(2018, 2021)),
      via = Seq("chat", "voice", "web", "email")(Random.nextInt(4))
    )
  }
}
