// import { xhRequest2 } from './base.js';
// *********************************************************************************************************************
// Desde truck
function newTruck() {
    return loadTruck(-1);
}

function delTruck() {

    let id       = document.getElementById("id").value;

    let data = JSON.stringify( { "id": id} );

    xhRequest2(true, "/entities/truck/delete/", delTruckOK, delTruckERROR, data);

} // delTruck
function delTruckOK(response) {
    newTruck();
}
function delTruckERROR(name) {
    alert("delTruck. ERROR "+ name);
}

// *********************************************************************************************************************
function truckCityClear() {
    document.getElementById("cityid").value   = "";
    document.getElementById("cityname").value = "";
}


// *********************************************************************************************************************
// Desde truckquerybody
function loadTruck(id) {
    xhRequest2(false, "/entities/truck/load/"+id, loadTruckOK, loadTruckERROR);
} // loadTruck
function loadTruckOK(response) {
    let divMain = document.getElementById("principal");
    divMain.innerHTML = response;
}
function loadTruckERROR(name) {
    alert("loadTruck. ERROR "+ name);
}


// *********************************************************************************************************************
function findTruck(mode) {

    let number = document.getElementById("querynumber").value;
    let status = document.getElementById("querystatus").value;
    let cityid = document.getElementById("querycityid").value;
    let capacity = document.getElementById("querycapacity").value;

    let data   = JSON.stringify( { "number": number, "status": status, "cityid": cityid, "capacity": capacity } );

    xhRequest2(true, "/entities/truck/find/"+mode, findTruckOK, findTruckERROR, data);

} // findTruck
function findTruckOK(response) {
    let divBody = document.getElementById("queryBody");
    divBody.innerHTML = response;
}
function findTruckERROR(name) {
    alert("findTruck. ERROR "+ name);
}


// *********************************************************************************************************************
function saveTruck() {

    let id       = document.getElementById("id").value;
    let number   = document.getElementById("number").value;
    let capacity = document.getElementById("capacity").value;
    let status   = document.getElementById("status").value;
    let cityId     = document.getElementById("cityid").value;

    let data = JSON.stringify( {"id": id, "number": number, "capacity": capacity, "status": status, "cityId": cityId} );

    xhRequest2(true, "/entities/truck/save/", saveTruckOK, saveTruckERROR, data);

} // saveTruck
function saveTruckOK(response) {
    let id = document.getElementById("id");
    id.value = response;
}
function saveTruckERROR(name) {
    alert("saveTruck. ERROR "+ name);
}


// *********************************************************************************************************************
function truckNumberSearch() {
    xhRequest2(false, "/queries/truck/head/0", truckNumberSearchOK, truckNumberSearchERROR);
}
function truckNumberSearchOK(response) {
    document.getElementById('consultas').innerHTML = response;
    document.getElementById('destinoId').value = 'id';

    document.getElementById('querynumber').value   = document.getElementById('number').value;
    document.getElementById('querystatus').value   = document.getElementById('status').value;
    document.getElementById('querycityid').value   = document.getElementById('cityid').value;
    document.getElementById('querycityname').value = document.getElementById('cityname').value;
    document.getElementById('querycapacity').value = document.getElementById('capacity').value;

}
function truckNumberSearchERROR(number) {
    alert("truckNumberSearch ERROR "+ number);
}

// *********************************************************************************************************************
function orderTruckSearch() {
    let recurso = '/queries/truck/head/1';
    xhRequest(recurso, orderTruckSearchOK, orderTruckSearchERROR);
}
function orderTruckSearchOK(response) {
    document.getElementById('consultas').innerHTML = response;
    document.getElementById('destinoId').value = 'truckid';

    document.getElementById('querynumber').value   = document.getElementById('trucknumber').value;
//    document.getElementById('querystatus').value   = document.getElementById('status').value;
    document.getElementById('querycityid').value   = document.getElementById('workcityid').value;
    document.getElementById('querycityname').value = document.getElementById('workcityname').value;
    document.getElementById('querycapacity').value = document.getElementById('weight').value;
}
function orderTruckSearchERROR(number) {
    alert("queryHeadERROR. ERROR "+ number);
}

// *********************************************************************************************************************

// From truck.html
function getTruckCity() {
    let id = document.getElementById("cityid").value;
    xhRequest("/queries/city/load/"+id, getTruckCityOK, getTruckCityERROR);
} // getTruckCity
function getTruckCityOK(response) {
    let city = JSON.parse(response);
    document.getElementById("cityname").value = city.name;
}
function getTruckCityERROR(number) {
    alert("getTruckCityERROR. ERROR "+ number);
}


