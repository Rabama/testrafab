

//
function loadActualUser() {
    xhRequest("/entities/user/actual/", loadActualUserOK, loadActualUserERROR);
} // loadActualUser
function loadActualUserOK(response) {
    let divMain = document.getElementById("principal");
    divMain.innerHTML = response;
}
function loadActualUserERROR(number) {
    alert("loadActualUserERROR. ERROR "+ number);
}

function userSave() {

    let id       = document.getElementById("userid").value;
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    let dataJson = { "id": id, "username": username, "password": password };

    let data = JSON.stringify( dataJson );

    xhRequest2(true, "/entities/user/save/", userSaveOK, userSaveERROR, data);

}
function userSaveOK(response) {
    alert("saved successfully: "+response);
}
function userSaveERROR(number) {
    alert("userSaveERROR. ERROR "+ number);
}




