var socket;
function openConnection() {
	socket = new WebSocket('ws://localhost:8080/Shopping_List_2/userSocket');
	socket.onmessage = function(message) {
		handleIncommingPacket(message);
	}
}
function handleIncommingPacket(message) {
	var information = message.data.split("\n");
	if (information[0].includes("UserResponsePacket")) {
		handleUserPacket(information);
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
	document.getElementById(previousLocation).style.display = 'none';
	document.getElementById('ListPage').style.display = 'inline';
}

function toHome(previousLocation){
	document.getElementById(previousLocation).style.display = 'none';
	document.getElementById('initialPage').style.display = 'inline';
}

function login() {
	var username = $('#logUsername').val();
	var password = $('#logPassword').val();
	var strPacket = "UserPacket\nLogin\n" + username + "\n" + password;
	socket.send(strPacket);
}

function signup(){
	var username = $('#logUsername').val();
	var password = $('#logPassword').val();
	var confirmPass = $('#signConfrim').val();
	var strPacket= "UserPacket\nAdd\n" + username + "\n" + password + "\n" + confirmPass;
	socket.send(strPacket);
}

function handleUserPacket(information) {
	if (information[2] == "true") {
		toListPage(information[4]);
	} else {
		document.getElementById("errorMessage").innerHTML = information[3];
	}
}