/**	
 * 
 */
function buildViewList(viewlist)
{
	let container = document.querySelector("#ticket_view_container tbody");
	console.log(container);
	if(container === null)
	{
		return;
	}
	let i = 0;
	for(i = 0; i < viewlist.length; i++)
	{
		let elem = viewlist[i];
		container.innerHTML += '<tr><th scope="row">'+ elem.ticketNumber + '</th><td>' + elem.type + '</td><td>' + elem.amount + '</td><td>' + elem.description + '</td><td>' + elem.status + '</td></tr>';
	}
};

function buildAdminList (listdata)
{
	let container = document.querySelector('#ticket_admin_list tbody');
	if(container === null)
	{
		return;
	}
	// container.innerHTML += '<table class="table"><thead><tr><th scope="col">Ticket #</th><th scope="col">Username</th><th scope="col">Type</th><th scope = "col">Amount</th><th scope = "col">Details</th><th scope="col">Action</th></thead><tbody>';
	let i = 0;
	for(i = 0; i < listdata.length; i++)
	{
		let elem = listdata[i];
		container.innerHTML += '<tr><th scope="row">'+ elem.ticketNumber +'</th>' + '<td>'+elem.username+ '</td>'+ '<td>'+ elem.type + '</td>' + '<td>'+ elem.amount + '</td>' + '<td>'+ elem.description + '</td>' + '<td><select name="ticket_' + elem.ticketNumber + '_action" id="ticket_' + elem.ticketNumber + '_action"><option value="nothing" selected>Do Nothing</option><option value="approve">Approve</option><option value="deny">Deny</option></select></td></tr>';		
	}
};