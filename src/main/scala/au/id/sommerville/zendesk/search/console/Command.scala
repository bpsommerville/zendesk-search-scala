package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.{SearchError, UnknownCommandError, UnknownSubCommandError}
import au.id.sommerville.zendesk.search.data.{Organization, Ticket}

trait Command

trait Entity {
}

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

  case class Search(entity: Entity, field: String, value: String) extends Command

  case class ListFields(entity: Entity) extends Command

  def parse(line: String): Either[SearchError,Command] = {
    line.split("\\s+").toList match {
      case ("h" | "help") :: Nil => Right(Help)
      case ("q" | "quit") :: Nil => Right(Quit)
      case ("s" | "search") :: e :: f :: v =>
        Entity.parse(e).map(
          e => Right(Search(e, f, v.mkString(" ")))
        ).getOrElse(Left(UnknownSubCommandError("search", (e :: f :: v).mkString(" "))))
      case ("f" | "fields") :: e :: Nil =>
        Entity.parse(e).map(
          e => Right(ListFields(e))
        ).getOrElse(Left(UnknownSubCommandError("fields", e)))
      case _ => Left(UnknownCommandError(line))
    }
  }

}