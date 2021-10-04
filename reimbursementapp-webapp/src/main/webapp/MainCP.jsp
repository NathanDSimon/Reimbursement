<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.1.4.0.css">
<script type="text/javascript" src="js/list_build_worker.js"></script>
<style>
	body {
  		background-color: #A2C4C9;
  	}
</style>
<meta charset="ISO-8859-1">
<title>Company X Reimbursement App: Home</title>
</head>
<body>
	<% if((Boolean)session.getAttribute("authenticated")) %>
		<div id = "cp">
			<% if(request.getAttribute("tickets_view") != null) { %>
			<div>
				<!-- JavaScript code will insert the table here -->
				<table class="table" id="ticket_view_container">
					<thead>
						<tr>
							<th scope="col">Ticket #</th>
							<th scope="col">Type</th>
							<th scope="col">Amount</th>
							<th scope="col">Details</th>
							<th scope="col">Status</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
				<script>
					let view = '<%= request.getAttribute("tickets_view")%>'
					view = view.replace(/&#39;/g, "'");
					buildViewList(JSON.parse(view));
				</script>
			</div>
			<% } else if(request.getAttribute("tickets_admin") != null) { %>
			<div id = "ticket_admin_container">
				<form action="/reimbursement/approvedeny" method="POST">
					<div>
						<!-- Javascript code will insert the form bits here -->
					<table class="table"  id="ticket_admin_list">
					<thead>
						<tr>
							<th scope="col">Ticket #</th>
							<th scope="col">User</th>
							<th scope="col">Type</th>
							<th scope="col">Amount</th>
							<th scope="col">Details</th>
							<th scope="col">Action</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
					</table>
					<script>
							let view = '<%= request.getAttribute("tickets_admin")%>'
							console.log(view);
							view = view.replace(/&#39;/g, "'");
							buildAdminList(JSON.parse(view));
						</script>
					</div>
					<input type="submit" value="Submit Changes">
				</form>
			</div>
			<% } %>
			<div id="cp_buttons">
			<% if ((Boolean)session.getAttribute("isAdmin")) { %>
				<!-- Admin buttons go here -->
				<form action="/reimbursement/createuser" method="POST">
					<h1>Create New User</h1>
					<div>
						<label>Username:</label>
						<input type="text" id="new_user_name" name="new_user_name">
					</div>
					<div>
						<label>Password:</label>
						<input type="password" id="new_user_pass" name="new_user_pass">
					</div>
					<div>
						<label>Account Type:</label>
						<select id="new_acct_type" name="new_acct_type">
							<option value="0" selected>Employee</option>
							<option value="1">Administrator</option>
						</select>
					</div>
					<div>
						<label>Email:</label>
						<input id="new_user_email" name="new_user_email">
					</div>
					<input type="submit" value="Create">
				</form>
				<form action="/reimbursement/ticketadmin" method="POST">
					<input type="submit" value="View Tickets Pending Approval">
				</form>
			<% } else { %>
				<!-- Non-admin buttons go here -->
				<form action="/reimbursement/ticketsubmission" method="POST">
					<h1>Submit A Ticket</h1>
					<div>
						<label>Ticket Type: </label>
						<select id="ticketType" name="ticketType">
							<option value="0" selected>Lodging</option>
							<option value="1">Travel</option>
							<option value="2">Food</option>
							<option value="3">Other</option>
						</select>
					</div>
					<div>
						<label>Amount: </label>
						<input name="amount" id="amount" type="number" min="0" step="0.01" value="0" required>
					</div>
					<div>
						<label>Expense Details: </label>
						<textarea rows="5" cols="50" id="desc" name="desc"></textarea>
					</div>
					<input type="submit" value="Submit Ticket">
				</form>
				<form action="/reimbursement/ticketview" method="POST">
					<input type="submit" value="View past and pending tickets">
				</form>
			<% } %>
			</div>
		</div>
</body>
</html>