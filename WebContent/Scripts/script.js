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
		handleEditForm(information);
	} else if (packetType.includes("List_NameResponsePacket")) {
		handleListNamePacket(information);
	} else if (packetType.includes("All_InfoResponsePacket")) {
		populateLists(information);
	} else if (packetType.includes("ItemResponsePacket")) {
		toListPage('EditPage')
	}
}

function populateLists(information) {
	var location = document.getElementById('lists');
	location.innerHTML = "";
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

			var table = createTable();
			for (j = 1; j <= listSize; ++j) {
				position += 2;
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
			}
			++position;
			var newRow = document.createElement('tr');
			var td = document.createElement('td');
			td.colSpan = 4;
			td.className = "total";
			td.innerHTML = "Total Price: $" + information[position];
			newRow.appendChild(td);
			table.appendChild(newRow);
			++position;
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

function handleEditForm(information) {
	var elementDiv = document.getElementById("EditPage");
	elementDiv.innerHTML = "";
	var listName = information[1];
	var listSize = information[2];
	var position = 3;
	var fieldset = createFieldset(listName);
	for (i = 0; i < listSize; ++i) {
		var item_ID = information[position];
		++position;
		var description = information[position];
		++position;
		var form = document.createElement('form');
		//
		var h2QElement = document.createElement('h2');
		var quantityDiv = document.createElement('div');
		quantityDiv.className = "title";
		quantityDiv.innerHTML = "Quantity:";
		h2QElement.appendChild(quantityDiv);
		form.appendChild(h2QElement);
		var quantityInput = document.createElement('input');
		quantityInput.type = "number";
		quantityInput.className = "textbox";
		quantityInput.setAttribute("name", "quantity");
		quantityInput.min = 1;
		quantityInput.value = information[position];
		form.appendChild(quantityInput);
		++position;
		//
		var h2IElement = document.createElement('h2');
		var itemDiv = document.createElement('div');
		itemDiv.className = "title";
		itemDiv.innerHTML = "Item:";
		h2IElement.appendChild(itemDiv);
		form.appendChild(h2IElement);
		var itemInput = document.createElement('input');
		itemInput.type = "text";
		itemInput.className = "textbox";
		itemInput.setAttribute("name", "item");
		itemInput.value = description;
		form.appendChild(itemInput);
		//
		var h2WElement = document.createElement('h2');
		var weightDiv = document.createElement('div');
		weightDiv.className = "title";
		weightDiv.innerHTML = "Weight:";
		h2WElement.appendChild(weightDiv);
		form.appendChild(h2WElement);
		var weightInput = document.createElement('input');
		weightInput.type = "number";
		weightInput.className = "textbox";
		weightInput.setAttribute("name", "weight");
		weightInput.min = 0;
		weightInput.step = .1;
		weightInput.value = information[position];
		form.appendChild(weightInput);
		++position;
		//
		var h2PElement = document.createElement('h2');
		var priceDiv = document.createElement('div');
		priceDiv.className = "title";
		priceDiv.innerHTML = "Price:";
		h2PElement.appendChild(priceDiv);
		form.appendChild(h2PElement);
		var priceInput = document.createElement('input');
		priceInput.type = "number";
		priceInput.className = "textbox";
		priceInput.setAttribute("name", "price");
		priceInput.min = 0;
		priceInput.step = .01;
		priceInput.value = information[position];
		form.appendChild(priceInput);
		++position;
		//
		var idInput = document.createElement('input');
		idInput.type = "hidden";
		idInput.value = item_ID;
		idInput.setAttribute("name", "item_ID");
		form.appendChild(idInput);
		//
		var originalInput = document.createElement('input');
		originalInput.type = "hidden";
		originalInput.value = description;
		originalInput.setAttribute("name", "original");
		form.appendChild(originalInput);
		//
		var lineBreak = document.createElement('br');
		form.appendChild(lineBreak);
		//
		var editInput = document.createElement('input');
		editInput.type = "button";
		editInput.value = "Edit this entry!";
		editInput.className = "submit";
		editInput.onclick = function() {
			editEntry(this);
		}
		form.appendChild(editInput);
		//
		var deleteInput = document.createElement('input');
		deleteInput.type = "button";
		deleteInput.value = "Delete this entry!";
		deleteInput.className = "submit";
		deleteInput.onclick = function() {
			deleteEntry(this);
		}
		form.appendChild(deleteInput);
		//
		fieldset.appendChild(form);

	}
	elementDiv.appendChild(fieldset);
}

function clearAddItem(){
	$('#itemQuantity').val('');
	$('#itemDescription').val('');
	$('#itemWeight').val('');
	$('#itemPrice').val('');
	$('#listName').val('');
}
function createFieldset(listName) {
	var currentFieldSet = document.createElement('fieldset');
	var legend = document.createElement('legend');
	legend.id = "editLegend";
	legend.innerHTML = listName;
	currentFieldSet.appendChild(legend);
	return currentFieldSet;
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
function toEditPage(previousLocation) {
	editList();
	document.getElementById('ListPage').style.display = 'none';
	document.getElementById('EditPage').style.display = 'inline';
}
function editList() {
	var listName = getSelectedList();
	var strPacket = "CommandPacket\nList_Info\n" + username + "\n" + listName;
	sendPacket(strPacket);
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
	document.getElementById('newListName').value = '';
	document.getElementById('ListPage').style.display = 'inline';
	clearAddItem();

}

function toHome(previousLocation) {
	document.getElementById(previousLocation).style.display = 'none';
	document.getElementById('InitialPage').style.display = 'inline';
	username = null;
}
function toAddItem(previousLocation, listName) {
	document.getElementById(previousLocation).style.display = 'none';
	document.getElementById('addListName').innerHTML = listName;
	document.getElementById('newListName').value = listName;
	document.getElementById('ItemPage').style.display = 'inline';
}
function toAddItem(previousLocation) {
	var listName = getSelectedList();
	document.getElementById(previousLocation).style.display = 'none';
	document.getElementById('addListName').innerHTML = listName;
	document.getElementById('newListName').value = listName;
	document.getElementById('ItemPage').style.display = 'inline';
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
	var listName = $('#newListName').val();
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
function editEntry(element) {
	var itemStr = getItemString(element);
	var strPacket = "Itempacket\n" + username + "\nEdit\n"
			+ document.getElementById("editLegend").innerHTML + "\n"
			+ getItemString(element);
	sendPacket(strPacket);
}

function deleteEntry(element) {
	var itemStr = getItemString(element);
	var strPacket = "Itempacket\n" + username + "\nDelete\n"
			+ document.getElementById("editLegend").innerHTML + "\n"
			+ getItemString(element);
	sendPacket(strPacket);
}
function getItemString(element) {
	var form = element.form;
	var item = form.elements[1].value;
	var quantity = form.elements["quantity"].value;
	var weight = form.elements["weight"].value;
	var price = form.elements["price"].value;
	var item_ID = form.elements["item_ID"].value;
	var original = form.elements["original"].value;
	return item_ID + "\n" + item + "\n" + quantity + "\n" + weight + "\n"
			+ price + "\n" + original;
}
function deleteList() {
	var listName = getSelectedList();
	var strPacket = "ListPacket\nDelete\n" + listName + "\n" + username;
	sendPacket(strPacket);
}

function addItem() {
	var listname = $('#newListName').val();
	var description = document.getElementById("itemDescription").value;
	var quantity = document.getElementById("itemQuantity").value;
	var weight = document.getElementById("itemWeight").value;
	var price = document.getElementById("itemPrice").value;
	var strPacket = "ListPacket\nAddItem\n" + listname + "\n" + username + "\n"
			+ description + "\n" + quantity + "\n" + weight + "\n" + price;
	sendPacket(strPacket);
}

function getSelectedList() {
	var selectElement = document.getElementById('listChoice');
	return selectElement.options[selectElement.selectedIndex].value;
}