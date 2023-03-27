// import { xhRequest2 } from './base.js';
// *********************************************************************************************************************
// Desde cargo
function newCargo() {
    return loadCargo(-1);
}

function delCargo() {

    let id       = document.getElementById("id").value;

    let data = JSON.stringify( { "id": id} );

    xhRequest2(true, "/entities/cargo/delete/", delCargoOK, delCargoERROR, data);

} // saveDistance
function delCargoOK(response) {
    newCargo();
}
function delCargoERROR(name) {
    alert("delCargo. ERROR "+ name);
}

// *********************************************************************************************************************
function cargoUpCityClear() {
    document.getElementById("upcity").value   = "";
    document.getElementById("upcityname").value = "";
}
function cargoUnCityClear() {
    document.getElementById("uncity").value   = "";
    document.getElementById("uncityname").value = "";
}


// *********************************************************************************************************************
// Desde cargoquerybody
function loadCargo(id) {
    xhRequest2(false, "/entities/cargo/load/"+id, loadCargoOK, loadCargoERROR);
} // loadCargo
function loadCargoOK(response) {
    let divMain = document.getElementById("principal");
    divMain.innerHTML = response;
}
function loadCargoERROR(name) {
    alert("loadCargo. ERROR "+ name);
}


// *********************************************************************************************************************
function findCargo(mode) {

    let name     = document.getElementById("queryname").value;
    let status   = document.getElementById("querystatus").value;
    let upcityid = document.getElementById("queryupcityid").value;
    let uncityid = document.getElementById("queryuncityid").value;
    let weight   = document.getElementById("queryweight").value;

    let data   = JSON.stringify( { "name": name, "status": status, "upcityid": upcityid, "uncityid": uncityid, "weight": weight } );

    xhRequest2(true, "/entities/cargo/find/"+mode, findCargoOK, findCargoERROR, data);

} // findCargo
function findCargoOK(response) {
    let divBody = document.getElementById("queryBody");
    divBody.innerHTML = response;
}
function findCargoERROR(name) {
    alert("findCargo. ERROR "+ name);
}


// *********************************************************************************************************************
function saveCargo() {

    let id     = document.getElementById("id").value;
    let name   = document.getElementById("name").value;
    let weight = document.getElementById("weight").value;
    let status = document.getElementById("status").value;
    let upcity = document.getElementById("upcity").value;
    let uncity = document.getElementById("uncity").value;

    let data = JSON.stringify( {"id": id, "name": name, "weight": weight, "status": status, "upcity": upcity, "uncity": uncity} );

    xhRequest2(true, "/entities/cargo/save/", saveCargoOK, saveCargoERROR, data);

} // saveCargo
function saveCargoOK(response) {
    let id = document.getElementById("id");
    id.value = response;
}
function saveCargoERROR(name) {
    alert("saveCargo. ERROR "+ name);
}


// *********************************************************************************************************************
function cargoSearch() {
    let recurso = '/queries/cargo/head/0';
    xhRequest(recurso, cargoSearchOK, cargoSearchERROR);
}
function cargoSearchOK(response) {
    document.getElementById('consultas').innerHTML = response;
    document.getElementById('destinoId').value = 'cargoid';

    document.getElementById('queryname').value       = document.getElementById('name').value;
    document.getElementById('querystatus').value     = document.getElementById('status').value;
    document.getElementById('queryupcityid').value   = document.getElementById('upcity').value;
    document.getElementById('queryupcityname').value = document.getElementById('upcityname').value;
    document.getElementById('queryuncityid').value   = document.getElementById('uncity').value;
    document.getElementById('queryuncityname').value = document.getElementById('uncityname').value;
    document.getElementById('queryweight').value     = document.getElementById('weight').value;

}
function cargoSearchERROR(number) {

}

// *********************************************************************************************************************

// From cargo.html
function getCargoUpCity() {
    let id = document.getElementById("upcity").value;
    xhRequest("/queries/city/load/"+id, getCargoUpCityOK, getCargoUpCityERROR);
} // loadCity
function getCargoUpCityOK(response) {
    let city = JSON.parse(response);
    document.getElementById("upcityname").value = city.name;
}
function getCargoUpCityERROR(number) {
    alert("getCargoUpCityERROR. ERROR "+ number);
}
// *********************************************************************************************************************

// From cargo.html
function getCargoUnCity() {
    let id = document.getElementById("uncity").value;
    xhRequest("/queries/city/load/"+id, getCargoUnCityOK, getCargoUnCityERROR);
} // loadCity
function getCargoUnCityOK(response) {
    let city = JSON.parse(response);
    document.getElementById("uncityname").value = city.name;
}
function getCargoUnCityERROR(number) {
    alert("getCargoUnCityERROR. ERROR "+ number);
}