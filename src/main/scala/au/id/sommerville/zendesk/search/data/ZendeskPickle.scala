package au.id.sommerville.zendesk.search.data

import java.time.{OffsetDateTime, ZoneId, ZoneOffset}
import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder, ResolverStyle}


object ZendeskPickle extends upickle.AttributeTagged {
  def camelToSnake(s: String) = {
    s.split("(?=[A-Z])", -1).map(_.toLowerCase).mkString("_")
  }

  def snakeToCamel(s: String) = {
    val res = s.split("(?<!^)_", -1).map(x => x(0).toUpper + x.drop(1)).mkString
    s(0).toLower + res.drop(1)
  }

  override def objectAttributeKeyReadMap(s: CharSequence) =
    snakeToCamel(s.toString)

  override def objectAttributeKeyWriteMap(s: CharSequence) =
    camelToSnake(s.toString)

  override def objectTypeKeyReadMap(s: CharSequence) =
    snakeToCamel(s.toString)

  override def objectTypeKeyWriteMap(s: CharSequence) =
    camelToSnake(s.toString)

  private val dateTimeFormatter = new DateTimeFormatterBuilder()
    .parseCaseInsensitive()
    .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    .parseLenient()
    .appendPattern("[ ][ZZZZZ]")
    .toFormatter()
    .withZone(ZoneId.ofOffset("", ZoneOffset.UTC))

  implicit val offsetDateTimeRw: ReadWriter[OffsetDateTime] = readwriter[String].bimap[OffsetDateTime](
    dt => dt.toString,
    str => OffsetDateTime.parse(str, dateTimeFormatter)
  )
}
