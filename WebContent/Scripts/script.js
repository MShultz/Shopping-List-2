var socket;
var username;
function openConnection() {
	socket = new WebSocket('ws://localhost:8080/Shopping_List_2/userSocket');
	socket.onmessage = function(message) {
		handleIncommingPacket(message);
	}
}
function handleIncommingPacket(message) {
	var information = message.data.split("\n");
	var packetType = information[0];
	if (packetType.includes("UserResponsePacket")) {
		handleUserPacket(information);
	} else if (packetType.includes("ListResponsePacket")) {
		handleListPacket(information);
	} else if (packetType.includes("List_InfoResponsePacket")) {

	} else if (packetType.includes("List_NameResponsePacket")) {
		handleListNamePacket(information);
	} else if (packetType.includes("All_InfoResponsePacket")) {
		populateLists(information);
	}
}

function populateLists(information) {
	var location = document.getElementById('lists');
	var numLists = information[2];
	if (numLists > 0) {
		var position = 3;
		for (i = 0; i < numLists; ++i) {
			var listName = information[position];
			++position;
			var listSize = information[position];
			var currentFieldSet = document.createElement('fieldset');
			currentFieldSet.id = 'currentLists';
			var legend = document.createElement('legend')
			legend.innerHTML = listName;
			currentFieldSet.appendChild(legend);

			position += 2;
			var table = createTable();
			for (j = 1; j <= listSize; ++j) {
				var row = table.insertRow(j);
				var cell1 = row.insertCell(0);
				var cell2 = row.insertCell(1);
				var cell3 = row.insertCell(2);
				var cell4 = row.insertCell(3);
				cell2.innerHTML = information[position];
				++position;
				cell1.innerHTML = information[position];
				++position;
				cell3.innerHTML = information[position];
				++position;
				cell4.innerHTML = information[position];
				++position
			}
			currentFieldSet.appendChild(table);
			location.appendChild(currentFieldSet);
		}
	} else
		document.getElementById('listMsg').innerHTML = "You currently have no lists";
}
function createTable() {
	var table = document.createElement('table');
	var tr = document.createElement('tr');
	var th1 = document.createElement('th');
	th1.innerHTML = "Quantity";
	tr.appendChild(th1);
	var th2 = document.createElement('th');
	th2.innerHTML = "Item";
	tr.appendChild(th2);
	var th3 = document.createElement('th');
	th3.innerHTML = "Weight";
	tr.appendChild(th3);
	var th4 = document.createElement('th');
	th4.innerHTML = "Price";
	tr.appendChild(th4);
	table.appendChild(tr);
	return table;
}
function handleListNamePacket(information) {
	var numLists = information[2];
	var listNames = new Array(numLists);
	var count = 0;
	for (i = 3; i < numLists + 3; ++i) {
		listNames[count] = information[i];
		++count;
	}
	fillListOptions(listNames, numLists);
}

function fillListOptions(listNames, numLists) {
	$('#listChoice').empty();
	for (i = 0; i < numLists; ++i) {
		$('#listChoice').append($('<option>', {
			value : listNames[i],
			text : listNames[i]
		}));
	}
}
function toSignUp(previousLocation) {
	document.getElementById(previousLocation).style.display = 'none';
	document.getElementById('SignupPage').style.display = 'inline';
}

function toLogin(previousLocation) {
	document.getElementById(previousLocation).style.display = 'none';
	document.getElementById('LoginPage').style.display = 'inline';
}

function toListPage(previousLocation) {
	requestListNames();
	document.getElementById(previousLocation).style.display = 'none';
	document.getElementById('userWelcome').innerHTML = "Welcome " + username
			+ "!";
	requestAllListInformation();
	document.getElementById('ListPage').style.display = 'inline';
}

function toHome(previousLocation) {
	document.getElementById(previousLocation).style.display = 'none';
	document.getElementById('InitialPage').style.display = 'inline';
	username = null;
}
function toAddItem(previousLocation, listName) {
	document.getElementById(previousLocation).style.display = 'none';
	document.getElementById('addListName').innerHTML = listName;
	document.getElementById('addListName').value = listName;
	document.getElementById('ItemPage').style.display = 'inline';
	document.getElementById('listName').value = "";
}
function toAddItem(previousLocation) {
	var listName = getSelectedList();
	document.getElementById(previousLocation).style.display = 'none';
	document.getElementById('addListName').innerHTML = listName;
	document.getElementById('addListName').value = listName;
	document.getElementById('ItemPage').style.display = 'inline';
	document.getElementById('listName').value = "";
}
function login() {
	username = $('#logUsername').val();
	var password = $('#logPassword').val();
	var strPacket = "UserPacket\nLogin\n" + username + "\n" + password;
	sendPacket(strPacket);
}

function signup() {
	username = $('#signUsername').val();
	var password = $('#signPass').val();
	var confirmPass = $('#signConfirm').val();
	var strPacket = "UserPacket\nAdd\n" + username + "\n" + password + "\n"
			+ confirmPass;
	sendPacket(strPacket);
}

function handleUserPacket(information) {
	if (information[2] == "true") {
		toListPage(information[4]);
	} else {
		if (information[4].includes("SignupPage"))
			document.getElementById("errorMessageS").innerHTML = information[3];
		else
			document.getElementById("errorMessageL").innerHTML = information[3];
	}
}

function handleListPacket(information) {
	if (information[3] == "true") {
		if (information[6].includes("CreatedList"))
			toAddItem('ListPage', information[1]);
		else if (information[6].includes("AddItem"))
			toListPage(information[5]);
		else if (information[6].includes("Delete"))
			requestListNames();
	} else {
		document.getElementById("errorMessage").innerHTML = information[4];
	}
}

function createList() {
	var listName = $('#listName').val();
	var strPacket = "ListPacket\nCreate\n" + listName + "\n" + username;
	sendPacket(strPacket);
}

function requestListNames() {
	var strPacket = "CommandPacket\nList_Name_All\n" + username;
	sendPacket(strPacket);
}
function requestAllListInformation() {
	var strPacket = "CommandPacket\nList_Info_All\n" + username;
	sendPacket(strPacket);
}

function sendPacket(strPacket) {
	socket.send(strPacket);
}

function deleteList() {
	var listName = getSelectedList();
	var strPacket = "ListPacket\nDelete\n" + listName + "\n" + username;
	sendPacket(strPacket);
}

function addItem() {
	var listname = $('#addListName').val();
	var description = document.getElementById("itemDescription").value;
	var quantity = document.getElementById("itemQuanity").value;
	var weight = document.getElementById("itemWeight").value;
	var price = document.getElementById("itemPrice").value;
	var strPacket = "ListPacket\nAddList\n" + listname + "\n" + username + "\n"
			+ description + "\n" + quantity + "\n" + weight + "\n" + price;
	sendPacket(strPacket);
}

function getSelectedList() {
	var selectElement = document.getElementById('listChoice');
	return selectElement.options[selectElement.selectedIndex].value;
}