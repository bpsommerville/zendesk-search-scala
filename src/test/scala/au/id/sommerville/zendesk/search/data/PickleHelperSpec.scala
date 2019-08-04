package au.id.sommerville.zendesk.search.data

import java.time.{OffsetDateTime, ZoneOffset}

import au.id.sommerville.zendesk.search.UnitTestSpec
import PickleHelper._
import org.scalatest.prop.TableDrivenPropertyChecks

/**
 *
 */
class PickleHelperSpec extends UnitTestSpec with TableDrivenPropertyChecks {

  val iso8601 = Table(
    ("s", "dt"),
    ("\"2011-06-23T12:03:05\"",OffsetDateTime.of(2011, 6, 23, 12, 3, 5, 0, ZoneOffset.ofHours(0))),
    ("\"2011-06-23T12:03:05Z\"",OffsetDateTime.of(2011, 6, 23, 12, 3, 5, 0, ZoneOffset.ofHours(0))),
    ("\"2011-06-23T12:03:05+00\"",OffsetDateTime.of(2011, 6, 23, 12, 3, 5, 0, ZoneOffset.ofHours(0))),
    ("\"2002-02-12T12:03:05+09\"",OffsetDateTime.of(2002, 2, 12, 12, 3, 5, 0, ZoneOffset.ofHours(9))),
    ("\"2002-02-12T12:03:05-11:30\"",OffsetDateTime.of(2002, 2, 12, 12, 3, 5, 0, ZoneOffset.ofHoursMinutes(-11, -30))),
  )

  "upickle" should "parse iso8601 datetime with offset string to OffsetDateTime" in {

    forAll(iso8601) { (s:String, dt:OffsetDateTime) =>
      upickle.default.read[OffsetDateTime](s) should equal (dt)
    }
  }

  val zendDesk = Table(
    ("s", "dt"),
    ("\"2011-06-23T12:03:05 +00\"",OffsetDateTime.of(2011, 6, 23, 12, 3, 5, 0, ZoneOffset.ofHours(0))),
    ("\"2002-02-12T12:03:05 +09:00\"",OffsetDateTime.of(2002, 2, 12, 12, 3, 5, 0, ZoneOffset.ofHours(9))),
    ("\"2002-02-12T12:03:05 -11:30\"",OffsetDateTime.of(2002, 2, 12, 12, 3, 5, 0, ZoneOffset.ofHoursMinutes(-11, -30)))
  )

  "upickle" should "parse ZenDesk datetime string to OffsetDateTime" in {

    forAll(zendDesk) { (s:String, dt:OffsetDateTime) =>
      upickle.default.read[OffsetDateTime](s) should equal (dt)
    }
  }

}
