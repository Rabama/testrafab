// *********************************************************************************************************************
//                                                       ADMIN
// *********************************************************************************************************************

// *********************************************************************************************************************
// ADMIN
// userqueryhead.html
function adminUserFind(mode) {
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
function adminLoadUser() {
//    alert("loadUser");
    xhRequest("/user/load/", adminLoadUserOK, adminLoadUserERROR);
} // loadUser
function adminLoadUserOK(response) {
//    alert("loadUserOK");
    let divMain = document.getElementById("principal");
    divMain.innerHTML = response;
}
function adminLoadUserERROR(number) {
    alert("loadUserERROR. ERROR "+ number);
}

// *********************************************************************************************************************
// ADMIN
// userquerybody.html
function adminLoadUserId(id) {
//    alert("adminLoadUserId " + id);
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
// ADMIN
function adminSaveUser() {
//    alert("adminSaveUser(): In.");

    let id       = document.getElementById("userid").value;
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let admin    = document.getElementById("admin").checked;
    let employee = document.getElementById("employee").checked;
    let driver   = document.getElementById("driver").checked;

    alert("id      : " + id         +
        "\nusername: " + username   +
        "\npassword: " + password +
        "\nadmin   : " + admin    +
        "\nemployee: " + employee +
        "\ndriver  : " + driver);

    let dataJson = { "id": id, "username": username, "password": password, "admin": admin, "employee": employee, "driver": driver };

    let data = JSON.stringify( dataJson );

//    alert(data);

    xhRequest2(true, "/user/save/", adminSaveUserOK, adminSaveUserERROR, data);

//    alert("adminSaveUser(): Out.");
} // saveUser
function adminSaveUserOK(response) {
    document.getElementById("userid").value = response;
    alert("saved successfully");
}
function adminSaveUserERROR(name) {
    alert("adminSaveUser: ERROR "+ name);
}

