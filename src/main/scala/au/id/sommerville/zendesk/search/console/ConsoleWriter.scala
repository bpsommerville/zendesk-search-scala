package au.id.sommerville.zendesk.search.console

import au.id.sommerville.zendesk.search.data.{Organization, ResolvedOrganization, ResolvedTicket, ResolvedUser, Ticket, User}

/**
 *
 */
object ConsoleWriter {


  def write(o: Organization): Seq[String] = write(ResolvedOrganization(o))
  def write(u: User): Seq[String] = write(ResolvedUser(u))

  def write(t: Ticket): Seq[String] = write(ResolvedTicket(t))

  def write(rt: ResolvedTicket): Seq[String] = Seq(
    Some(s"_id:              ${rt.ticket._id}"),
    Some(s"url:              ${rt.ticket.url}"),
    Some(s"external_id:      ${rt.ticket.externalId}"),
    Some(s"created_at:       ${rt.ticket.createdAt}"),
    rt.ticket.`type`.map(v => s"type:             ${v}"),
    Some(s"subject:          ${rt.ticket.subject}"),
    rt.ticket.description.map(v => s"description:      ${v}"),
    Some(s"priority:         ${rt.ticket.priority}"),
    Some(s"status:           ${rt.ticket.status}"),
    Some(s"submitter_id:     ${rt.ticket.submitterId}${writeUserSummary(rt.submitter)}"),
    rt.ticket.assigneeId.map(v => s"assignee_id:      ${v}${writeUserSummary(rt.assignee)}"),
    rt.ticket.organizationId.map(v => s"organization_id:  ${v}${writeOrgSummary(rt.organization)}"),
    Some(s"tags:             [ ${rt.ticket.tags.mkString(", ")} ]"),
    Some(s"has_incidents:    ${rt.ticket.hasIncidents}"),
    rt.ticket.dueAt.map(v => s"due_at:           ${v}"),
    Some(s"via:              ${rt.ticket.via}")
  ).flatten

  def write(ro: ResolvedOrganization):Seq[String] = Seq(
      Some(s"_id:             ${ro.organization._id}"),
      Some(s"url:             ${ro.organization.url}"),
      Some(s"external_id:     ${ro.organization.externalId}"),
      Some(s"name:            ${ro.organization.name}"),
      Some(s"domain_names:    [ ${ro.organization.domainNames.mkString(", ")} ]"),
      Some(s"created_at:      ${ro.organization.createdAt}"),
      Some(s"details:         ${ro.organization.details}"),
      Some(s"shared_tickets:  ${ro.organization.sharedTickets}"),
      Some(s"tags:            [ ${ro.organization.tags.mkString(", ")} ]"),
      writeUsersSummary(ro.users,15),
      writeTicketsSummary(ro.tickets,"tickets", 15)
    ).flatten

  def write(ru: ResolvedUser): Seq[String] = Seq(
     Some(s"_id:              ${ru.user._id}"),
     Some(s"url:              ${ru.user.url}"),
     Some(s"external_id:      ${ru.user.externalId}"),
     Some(s"name:             ${ru.user.name}"),
     ru.user.alias.map(v => s"alias:            ${v}"),
     Some(s"created_at        ${ru.user.createdAt}"),
     Some(s"active:           ${ru.user.active}"),
     ru.user.verified.map(v => s"verified:         ${v}"),
     Some(s"shared:           ${ru.user.shared}"),
     ru.user.locale.map(v => s"locale:           ${v}"),
     ru.user.timezone.map(v => s"timezone:         ${v}"),
     Some(s"last_login_at:    ${ru.user.lastLoginAt}"),
     ru.user.email.map(v => s"email:            ${v}"),
     Some(s"phone:            ${ru.user.phone}"),
     Some(s"signature:        ${ru.user.signature}"),
     ru.user.organizationId.map(v => s"organization_id:  ${v}${writeOrgSummary(ru.organization)}"),
     Some(s"tags:             [ ${ru.user.tags.mkString(", ")} ]"),
     Some(s"suspended:        ${ru.user.suspended}"),
     Some(s"role:             ${ru.user.role}"),
     writeTicketsSummary(ru.submittedTickets, "submitted_tickets",16),
     writeTicketsSummary(ru.assignedTickets, "assigned_tickets",16),
   ).flatten

  def writeUsersSummary(users: Option[Seq[User]], indent: Int): Seq[String] = {
    val leftPad = "".padTo(indent, ' ')
    users.map(users => {
      Seq("users:") ++
        users.map(u => s"${leftPad}- ${u.name} : ${u._id}")
    }).getOrElse(Seq())
  }

  def writeTicketsSummary(tickets: Option[Seq[Ticket]], label:String, indent:Int): Seq[String] = {
    val leftPad = "".padTo(indent, ' ')
    tickets.map(tickets => {
      Seq(s"${label}:") ++
        tickets.map(t => s"${leftPad}- ${t.subject} : ( ${t.priority}, ${t.status} ) : ${t._id}")
    }).getOrElse(Seq())
  }

  def writeOrgSummary(org: Option[Organization]) =
    s"${org.map(o => s" ( ${o.name} )").getOrElse("")}"

  def writeUserSummary(user: Option[User]) =
    s"${user.map(o => s" ( ${o.name} )").getOrElse("")}"
}
