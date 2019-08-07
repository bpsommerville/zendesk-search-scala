package au.id.sommerville.zendesk.search.console

trait Command

trait Entity

object Entity {

  case object Tickets extends Entity

  case object Organizations extends Entity

  case object Users extends Entity

}


object Command {

  case object Quit extends Command

  case object Help extends Command

  case object Unknown extends Command

  case class Search(
    entity: Entity,
    field: String,
    value: String
  ) extends Command

  case class ListFields(entity: Entity) extends Command

  def apply(line: String): Command = {
    line match {
      case "h" | "help" => Help
      case "q" | "quit" => Quit
      case _ => Unknown
    }
  }

}