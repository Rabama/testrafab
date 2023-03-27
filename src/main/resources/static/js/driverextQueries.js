// import { xhRequest2 } from './base.js';

const ctId      = 0;
const ctName    = 1;
const ctUpcity  = 2;
const ctUncity  = 3;
const ctWeight  = 4;
const ctUpload  = 5;
const ctUnload  = 6;
const ctStatus  = 7;
const ctDriver1 = 8;
const ctDriver2 = 9;
const ctDriver3 = 10;




// *********************************************************************************************************************
// Desde
function loadDriverExt() {
    xhRequest2(false, "/drivers/load/", loadDriverExtOK, loadDriverExtERROR);
} // loadDriverExt
function loadDriverExtOK(response) {
    if (response.substring(0, 5) === "Error") {
        alert(response);
    } else {
        let divMain = document.getElementById("principal");
        divMain.innerHTML = response;
        let divQuery = document.getElementById("consultas");
        divQuery.innerHTML = "";
    }
}
function loadDriverExtERROR(name) {
    alert("loadDriver. ERROR "+ name);
}


// *********************************************************************************************************************
function loadOrderDriverExt() {
    xhRequest2(false, "/drivers/order/load/", loadOrderDriverExtOK, loadOrderDriverExtERROR);
} // loadOrderDriverExt
function loadOrderDriverExtOK(response) {
    let divMain = document.getElementById("principal");
    divMain.innerHTML = response;
}
function loadOrderDriverExtERROR(name) {
    alert("loadDriver. ERROR "+ name);
}

// *********************************************************************************************************************
function loadWaypointDriverExt(target) {
    xhRequest2(false, "/drivers/waypoint/load/"+target, loadCargoDriverExtOK, loadCargoDriverExtERROR);
}
function loadCargoDriverExtOK(response) {
    let divMain = document.getElementById("consultas");
    divMain.innerHTML = response;
}
function loadCargoDriverExtERROR(name) {
    alert("loadCargoDriverExt. ERROR "+ name);
}

// *********************************************************************************************************************
function saveDriverExt() {

    let id       = document.getElementById("id").value;
    let name     = document.getElementById("name").value;
    let surname  = document.getElementById("surname").value;
    let workedh  = document.getElementById("workedhours").value;
    let status   = document.getElementById("status").value;
    let password = document.getElementById("password").value;

    workedh = (Math.round(workedh * 100) / 100).toString();

    let data = JSON.stringify( {"id": id, "name": name, "surname": surname, "workedhours": workedh, "status": status, "password": password} );

    xhRequest2(true, "/drivers/save/", saveDriverExtOK, saveDriverExtERROR, data);

} // saveDriverExt
function saveDriverExtOK(response) {
}
function saveDriverExtERROR(name) {
    xhRequest2(false, "/drivers/load/", loadDriverExtOK, loadDriverExtERROR);
    alert("saveDriver. ERROR "+ name);
}


// *********************************************************************************************************************
function saveCargoOrder() {


    let waypointId = document.getElementById("waypointId").value;
    let driver1 = document.getElementById("driver1").value;
    let driver1Id = driver1.substring(0, driver1.indexOf(":"));
    alert("driver1id" + driver1Id);
    let driver2 = document.getElementById("driver2").value;
    let driver2Id = driver2.substring(0, driver2.indexOf(":"));
    let driver3 = document.getElementById("driver3").value;
    let driver3Id = driver3.substring(0, driver3.indexOf(":"));
    let hours   = document.getElementById("workedhours").value;

    let data = JSON.stringify({"driver1Id": driver1Id, "driver2Id": driver2Id, "driver3Id": driver3Id, "hours": hours, "waypointId": waypointId} );

    xhRequest2(true, "/drivers/waypoint/order/save/", saveCargoOrderOK, saveCargoOrderERROR, data);

} //saveCargoOrder
function saveCargoOrderOK(response) {
    if (response.substring(0, 5) === "Error") {
        alert(response);
    } else{
        alert("Changes saved.");
        xhRequest2(false, "/drivers/order/load/", loadOrderDriverExtOK, loadOrderDriverExtERROR);
    }
}
function saveCargoOrderERROR(name) {
    alert("saveCargoOrder. ERROR "+ name);
}
