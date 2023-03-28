function UserFind(mode) {
    let username = document.getElementById("queryusername").value;
    let data = JSON.stringify( { "username": username } );
    xhRequest2(true, "/user/find/"+mode, findUserOK, findUserERROR, data);
} // findCity
function findUserOK(response) {
    let divBody = document.getElementById("queryBody");
    divBody.innerHTML = response;
}
function findUserERROR(name) {
    alert("findUser. ERROR "+ name);
}

// *********************************************************************************************************************
// ADMIN
// index.html
function LoadUser() {
//    alert("loadUser");
    xhRequest("/user/load/", adminLoadUserOK, adminLoadUserERROR);
} // loadUser
function LoadUserOK(response) {
//    alert("loadUserOK");
    let divMain = document.getElementById("principal");
    divMain.innerHTML = response;
}
function LoadUserERROR(number) {
    alert("loadUserERROR. ERROR "+ number);
}

// *********************************************************************************************************************
// ADMIN
// userquerybody.html
function LoadUserId(id) {
//    alert("LoadUserId " + id);
    xhRequest("/user/load/"+id, loadUsersOK, loadUsersERROR);
} //
function loadUsersOK(response) {
//    alert("loadUsersOK");
    let divMain = document.getElementById("principal");
    divMain.innerHTML = response;
}
function loadUsersERROR(number) {
    alert("loadUsersERROR. ERROR "+ number);
}

// *********************************************************************************************************************

function saveUser() {
//    alert("adminSaveUser(): In.");

    let id       = document.getElementById("userid").value;
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    alert("id      : " + id         +
        "\nusername: " + username   +
        "\npassword: " + password);

    let dataJson = { "id": id, "username": username, "password": password};

    let data = JSON.stringify( dataJson );

//    alert(data);

    xhRequest2(true, "/entities/user/save/", SaveUserOK, SaveUserERROR, data);

//    alert("adminSaveUser(): Out.");
} // saveUser
function SaveUserOK(response) {
//    document.getElementById("driver3name").value = JSON.parse(response).name;
    alert("saved successfully: "+response);
}
function SaveUserERROR(name) {
    alert("SaveUser: ERROR "+ name);
}

