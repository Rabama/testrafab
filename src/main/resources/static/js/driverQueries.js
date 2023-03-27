// import { xhRequest2 } from './base.js';


// *********************************************************************************************************************
// Desde driver.

function newDriver() {
    return loadDriver(-1);
}

function delDriver() {

    let id       = document.getElementById("id").value;

    let data = JSON.stringify( { "id": id} );

    xhRequest2(true, "/entities/driver/delete/", delDriverOK, delDriverERROR, data);

} // delDriver
function delDriverOK(response) {
    newDriver();
}
function delDriverERROR(name) {
    alert("delDriver. ERROR "+ name);
}


function driverCityClear() {
    document.getElementById("cityid").value   = "";
    document.getElementById("cityname").value = "";
}



// *********************************************************************************************************************
// Desde
function loadDriver(id) {
    xhRequest2(false, "/entities/driver/load/"+id, loadDriverOK, loadDriverERROR);
} // loadDriver
function loadDriverOK(response) {
    let divMain = document.getElementById("principal");
    divMain.innerHTML = response;
}
function loadDriverERROR(name) {
    alert("loadDriver. ERROR "+ name);
}


// *********************************************************************************************************************
function findDriver(mode) {

    let name   = document.getElementById("queryname").value;
    let status = document.getElementById("querystatus").value;
    let cityid = document.getElementById("querycityid").value;

    let data = JSON.stringify( { "name": name, "status": status, "cityid": cityid } );

    xhRequest2(true, "/entities/driver/find/"+mode, findDriverOK, findDriverERROR, data);

} // findDriver
function findDriverOK(response) {
    let divBody = document.getElementById("queryBody");
    divBody.innerHTML = response;
}
function findDriverERROR(name) {
    alert("findDriver. ERROR "+ name);
}


// *********************************************************************************************************************
function saveDriver() {

    let id       = document.getElementById("id").value;
    let name     = document.getElementById("name").value;
    let surname  = document.getElementById("surname").value;
    let workedh  = document.getElementById("workedhours").value;
    let status   = document.getElementById("status").value;
    let username = document.getElementById("username").value;
    let cityid   = document.getElementById("cityid").value;

//    alert("Username " + username)

    let data = JSON.stringify( { "id": id, "name": name, "surname": surname, "workedhours": workedh, "status": status, "username": username, "cityid": cityid } );

    xhRequest2(true, "/entities/driver/save/", saveDriverOK, saveDriverERROR, data);

} // saveDriver
function saveDriverOK(response) {
    let id = document.getElementById("id");
    id.value = response;
}
function saveDriverERROR(name) {
    alert("saveDriver. ERROR "+ name);
}


// *********************************************************************************************************************
function driverNameSearch() {
    xhRequest2(false, "/queries/driver/head/0", driverNameSearchOK, driverNameSearchERROR);
}
function driverNameSearchOK(response) {
    document.getElementById('consultas').innerHTML = response;
    document.getElementById('destinoId').value     = 'id';

    document.getElementById('queryname').value     = document.getElementById('name').value;
    document.getElementById('querystatus').value   = document.getElementById('status').value;

    document.getElementById('querycityid').value   = document.getElementById('cityid').value;
    document.getElementById('querycityname').value = document.getElementById('cityname').value;

}
function driverNameSearchERROR(number) {
    alert("driverNameSearch: ERROR "+ number);
}


// *********************************************************************************************************************
function orderDriverSearch(num) {
    let recurso = '/queries/driver/head/1';
    switch (num) {
        case 1:
            xhRequest(recurso, orderDriver1SearchOK, orderDriverSearchERROR);
            break;
        case 2:
            xhRequest(recurso, orderDriver2SearchOK, orderDriverSearchERROR);
            break;
        case 3:
            xhRequest(recurso, orderDriver3SearchOK, orderDriverSearchERROR);
            break;
    }

}
function orderDriver1SearchOK(response) {
    document.getElementById('consultas').innerHTML = response;
    document.getElementById('destinoId').value     = 'driver1id';

    document.getElementById('queryname').value     = document.getElementById('driver1name').value;
    document.getElementById('querycityid').value   = document.getElementById('workcityid').value;
    document.getElementById('querycityname').value = document.getElementById('workcityname').value;
}
function orderDriver2SearchOK(response) {
    document.getElementById('consultas').innerHTML = response;
    document.getElementById('destinoId').value     = 'driver2id';

    document.getElementById('queryname').value     = document.getElementById('driver2name').value;
    document.getElementById('querycityid').value   = document.getElementById('workcityid').value;
    document.getElementById('querycityname').value = document.getElementById('workcityname').value;
}
function orderDriver3SearchOK(response) {
    document.getElementById('consultas').innerHTML = response;
    document.getElementById('destinoId').value     = 'driver3id';

    document.getElementById('queryname').value     = document.getElementById('driver3name').value;
    document.getElementById('querycityid').value   = document.getElementById('workcityid').value;
    document.getElementById('querycityname').value = document.getElementById('workcityname').value;
}
function orderDriverSearchERROR(number) {
    alert("queryHeadERROR. ERROR "+ number);
}

// *********************************************************************************************************************

// Desde driver.html
function getDriverCity() {
    let id = document.getElementById("cityid").value;
    xhRequest("/queries/city/load/"+id, getDriverCityOK, getDriverCityERROR);
} //
function getDriverCityOK(response) {
    let city = JSON.parse(response);
    document.getElementById("cityname").value = city.name;
}
function getDriverCityERROR(number) {
    alert("getDriverCityERROR. ERROR "+ number);
}
