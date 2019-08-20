package au.id.sommerville.zendesk.search.data

import java.time.{OffsetDateTime, ZoneOffset}

import au.id.sommerville.zendesk.search.UnitTestSpec
import org.scalatest.prop.TableDrivenPropertyChecks

/**
 *
 */
class ZendeskPickleSpec extends UnitTestSpec with TableDrivenPropertyChecks {

  val iso8601 = Table(
    ("s", "dt"),
    ("\"2011-06-23T12:03:05\"", OffsetDateTime.of(2011, 6, 23, 12, 3, 5, 0, ZoneOffset.ofHours(0))),
    ("\"2011-06-23T12:03:05Z\"", OffsetDateTime.of(2011, 6, 23, 12, 3, 5, 0, ZoneOffset.ofHours(0))),
    ("\"2011-06-23T12:03:05+00\"", OffsetDateTime.of(2011, 6, 23, 12, 3, 5, 0, ZoneOffset.ofHours(0))),
    ("\"2002-02-12T12:03:05+09\"", OffsetDateTime.of(2002, 2, 12, 12, 3, 5, 0, ZoneOffset.ofHours(9))),
    ("\"2002-02-12T12:03:05-11:30\"", OffsetDateTime.of(2002, 2, 12, 12, 3, 5, 0, ZoneOffset.ofHoursMinutes(-11, -30))),
  )

  "ZendeskPickle" should "parse iso8601 datetime with offset string to OffsetDateTime" in {

    forAll(iso8601) { (s: String, dt: OffsetDateTime) =>
      ZendeskPickle.read[OffsetDateTime](s) should equal(dt)
    }
  }

  val zendDesk = Table(
    ("s", "dt"),
    ("\"2011-06-23T12:03:05 +00\"", OffsetDateTime.of(2011, 6, 23, 12, 3, 5, 0, ZoneOffset.ofHours(0))),
    ("\"2002-02-12T12:03:05 +09:00\"", OffsetDateTime.of(2002, 2, 12, 12, 3, 5, 0, ZoneOffset.ofHours(9))),
    ("\"2002-02-12T12:03:05 -11:30\"", OffsetDateTime.of(2002, 2, 12, 12, 3, 5, 0, ZoneOffset.ofHoursMinutes(-11, -30)))
  )

  "ZendeskPickle" should "parse ZenDesk datetime string to OffsetDateTime" in {

    forAll(zendDesk) { (s: String, dt: OffsetDateTime) =>
      ZendeskPickle.read[OffsetDateTime](s) should equal(dt)
    }
  }

  case class Thing(_id: Int, myFieldA: Int, myFieldB: String, nocase: String)

  "ZendeskPickle" should "use snake case for json and camel case for scala" in {
    implicit def thingRW: ZendeskPickle.ReadWriter[Thing] = ZendeskPickle.macroRW

    // snake_case_keys read-writing
    ZendeskPickle.write(Thing(10, 1, "gg", "foo")) should equal("""{"_id":10,"my_field_a":1,"my_field_b":"gg","nocase":"foo"}""")

    ZendeskPickle.read[Thing]("""{"_id":10,"my_field_a":1,"my_field_b":"gg","nocase":"foo"}""") should equal(Thing(10, 1, "gg", "foo"))
  }

  case class OptionalThing(_id: Int, myFieldA: Option[Int] = None, myFieldB: Option[String] = None)

  "ZendeskPickle" should "use accept missing values for optional fields" in {
    implicit def thingRW: ZendeskPickle.ReadWriter[OptionalThing] = ZendeskPickle.macroRW


    ZendeskPickle.write(OptionalThing(10, Some(1), Some("gg"))) should equal("""{"_id":10,"my_field_a":1,"my_field_b":"gg"}""")
    ZendeskPickle.write(OptionalThing(10, None, None)) should equal("""{"_id":10}""")

    ZendeskPickle.read[OptionalThing]("""{"_id":10,"my_field_a":1,"my_field_b":"gg"}""") should equal(OptionalThing(10, Some(1), Some("gg")))
    ZendeskPickle.read[OptionalThing]("""{"_id":10}""") should equal(OptionalThing(10, None, None))
  }

}
