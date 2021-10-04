<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<% if(!(Boolean)session.getAttribute("authenticated")) response.sendRedirect("/reimbursement");%>
<link rel="stylesheet" href="css/bootstrap.min.1.4.0.css">
<link rel="stylesheet" href="css/main.css">
<script type="text/javascript" src="js/list_build_worker.js"></script>
<meta charset="ISO-8859-1">
<title>Company X Reimbursement App: Home</title>
</head>
<body>
	<% if((Boolean)session.getAttribute("authenticated"))  { %>
		<div class="page-container">
			<div id="cp_buttons">
			<% if ((Boolean)session.getAttribute("isAdmin")) { %>
				<!-- Admin buttons go here -->
				<div class="form-flex-container">
					<form action="/reimbursement/createuser" method="POST">
						<h2>Create New User</h2>
						<div class="form-element-container">
							<label>Username:</label>
							<input type="text" id="new_user_name" name="new_user_name">
						</div>
						<div class="form-element-container">
							<label>Password:</label>
							<input type="password" id="new_user_pass" name="new_user_pass">
						</div>
						<div class="form-element-container">
							<label>Account Type:</label>
							<select id="new_acct_type" name="new_acct_type">
								<option value="0" selected>Employee</option>
								<option value="1">Administrator</option>
							</select>
						</div>
						<div class="form-element-container">
							<label>Email:</label>
							<input id="new_user_email" name="new_user_email">
						</div>
						<div class="button-container"><input type="submit" value="Create"></div>
					</form>
				</div>
				<div class="form-flex-container">
					<form action="/reimbursement/chpass" method="POST">
						<h2>Change Account Password</h2>
						<div class="form-element-container">
							<label>Old Password:</label>
							<input type="password" id="passconfirm" name="passconfirm">
						</div>
						<div class="form-element-container">
							<label>New Password:</label>
							<input type="password" id="newpass" name="newpass">
						</div>
						<div class="button-container">
							<input type="submit" value="Change">
						</div>
					</form>
				</div>
				<form action="/reimbursement/ticketadmin" method="POST">
					<input type="submit" value="View Tickets Pending Approval">
				</form>
			<% } else { %>
				<!-- Non-admin buttons go here -->
				<div class="form-flex-container">
					<form action="/reimbursement/ticketsubmission" method="POST">
						<h2>Submit A Ticket</h2>
						<div class="form-element-container">
							<label>Ticket Type: </label>
							<select id="ticketType" name="ticketType">
								<option value="0" selected>Lodging</option>
								<option value="1">Travel</option>
								<option value="2">Food</option>
								<option value="3">Other</option>
							</select>
						</div>
						<div class="form-element-container">
							<label>Amount: </label>
							<input name="amount" id="amount" type="number" min="0" step="0.01" value="0" required>
						</div>
						<div class="form-element-container">
							<label>Expense Details: </label>
							<textarea rows="5" cols="50" id="desc" name="desc"></textarea>
						</div>
						<div class="button-container"><input type="submit" value="Submit Ticket"></div>
					</form>
				</div>
				<div class="form-flex-container">
					<form action="/reimbursement/chpass" method="POST">
						<h2>Change Account Password</h2>
						<div class="form-element-container">
							<label>Old Password:</label>
							<input type="password" id="passconfirm" name="passconfirm">
						</div>
						<div class="form-element-container">
							<label>New Password:</label>
							<input type="password" id="newpass" name="newpass">
						</div>
						<div class="button-container">
							<input type="submit" value="Change">
						</div>
					</form>
				</div>
				<div>
					<form action="/reimbursement/ticketview" method="POST">
						<input type="submit" value="View past and pending tickets">
					</form>
				</div>
			<% } %>
			</div>
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
			<a href="/reimbursement/logout">Log Out</a>
		</div>
		<%} %>
</body>
</html>