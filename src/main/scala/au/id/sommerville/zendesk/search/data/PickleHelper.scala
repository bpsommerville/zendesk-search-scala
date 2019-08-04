package au.id.sommerville.zendesk.search.data

import java.time.chrono.IsoChronology
import java.time.{OffsetDateTime, ZoneId, ZoneOffset}
import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder, ResolverStyle}

import upickle.default.{ReadWriter, macroRW}

/**
 *
 */
object PickleHelper {
  private val dateTimeFormatter = new DateTimeFormatterBuilder()
    .parseCaseInsensitive()
    .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    .parseLenient()
    .appendPattern("[ ][ZZZZZ]")
    .toFormatter()
    .withZone(ZoneId.ofOffset("", ZoneOffset.UTC))

  implicit val offsetDateTimeRw: ReadWriter[OffsetDateTime] = upickle.default.readwriter[String].bimap[OffsetDateTime](
    dt => dt.toString,
    str => OffsetDateTime.parse(str, dateTimeFormatter)
  )
}
