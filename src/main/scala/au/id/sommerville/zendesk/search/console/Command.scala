package au.id.sommerville.zendesk.search.console

trait Command

trait Entity

object Entity {

  case object Tickets extends Entity

  case object Organizations extends Entity

  case object Users extends Entity

  def parse(s: String): Option[Entity] = {
    s match {
      case "o" | "org" | "organization" => Some(Organizations)
      case "t" | "ticket" => Some(Tickets)
      case "u" | "user" => Some(Users)
      case _ => None
    }
  }

}


object Command {

  case object Quit extends Command

  case object Help extends Command

  case object Unknown extends Command

  case class Search(entity: Entity, field: String, value: String) extends Command

  case class ListFields(entity: Entity) extends Command

  def parse(line: String): Command = {
    line.split("\\s+").toList match {
      case ("h" | "help") :: Nil => Help
      case ("q" | "quit") :: Nil => Quit
      case ("s" | "search") :: e :: f :: v =>
        Entity.parse(e).map(
          e => Search(e, f, v.mkString(" "))
        ).getOrElse(Unknown)
      case ("f" | "fields") :: e :: Nil =>
        Entity.parse(e).map(
          e => ListFields(e)
        ).getOrElse(Unknown)
      case _ => Unknown
    }
  }

}