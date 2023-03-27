// import { xhRequest2 } from './base.js';

// *********************************************************************************************************************
// Desde city
function newCity() {
    loadCity(-1);
}

function delCity() {

    let id       = document.getElementById("id").value;

    let data = JSON.stringify( { "id": id} );

    xhRequest2(true, "/entities/city/delete/", delCityOK, delCityERROR, data);

} // delCity
function delCityOK(response) {
    newCity();
}
function delCityERROR(name) {
    alert("delCity. ERROR "+ name);
}

// *********************************************************************************************************************
// Desde cityquerybody
function loadCity(id) {
    xhRequest2(false, "/entities/city/load/"+id, loadCityOK, loadCityERROR);
} // loadCity
function loadCityOK(response) {
    let divMain = document.getElementById("principal");
    divMain.innerHTML = response;
}
function loadCityERROR(name) {
    alert("loadCity. ERROR "+ name);
}


// *********************************************************************************************************************
function findCity(mode) {

    let name   = document.getElementById("queryname").value;

    let data   = JSON.stringify( {"name": name} );

    xhRequest2(true, "/entities/city/find/"+mode, findCityOK, findCityERROR, data);

} // findCity
function findCityOK(response) {
    let divBody = document.getElementById("queryBody");
    divBody.innerHTML = response;
}
function findCityERROR(name) {
    alert("findCity. ERROR "+ name);
}


// *********************************************************************************************************************
function saveCity() {

    let id     = document.getElementById("id").value;
    let name   = document.getElementById("name").value;

    let data = JSON.stringify( {"id": id, "name": name} );

    xhRequest2(true, "/entities/city/save/", saveCityOK, saveCityERROR, data);

} // saveCity
function saveCityOK(response) {
    let id = document.getElementById("id");
    id.value = xhr.responseText;
}
function saveCityERROR(name) {
    alert("saveCity. ERROR "+ name);
}
