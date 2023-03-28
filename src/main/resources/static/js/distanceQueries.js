// *********************************************************************************************************************
// Desde distance
function newDistance() {
    return loadDistance(-1);
}

function delDistance() {

    let id       = document.getElementById("id").value;

    let data = JSON.stringify( { "id": id} );

    xhRequest2(true, "/entities/distance/delete/", delDistanceOK, delDistanceERROR, data);

} // saveDistance
function delDistanceOK(response) {
    newDistance();
}
function delDistanceERROR(name) {
    alert("delDistance. ERROR "+ name);
}

// *********************************************************************************************************************
function distanceCity0Clear() {
    document.getElementById("city0").value   = "";
    document.getElementById("cityname0").value = "";
}
function distanceCity1Clear() {
    document.getElementById("city1").value   = "";
    document.getElementById("cityname1").value = "";
}

// *********************************************************************************************************************
function loadDistance(id) {
    xhRequest2(false, "/entities/distance/load/"+id, loadDistanceOK, loadDistanceERROR);
} // loadCity
function loadDistanceOK(response) {
    let divMain = document.getElementById("principal");
    divMain.innerHTML = response;
}
function loadDistanceERROR(name) {
    alert("loadDistance. ERROR "+ name);
}
// *********************************************************************************************************************
// Desde distance
function distanceSearch() {
    xhRequest2(false, '/queries/distance/head/0', distanceSearchOK, distanceSearchERROR);
}
function distanceSearchOK(response) {
    document.getElementById('consultas').innerHTML = response;
    document.getElementById('destinoId').value = 'id';

    document.getElementById('querydistance').value  = document.getElementById('distance').value;
}
function distanceSearchERROR(number) {
    alert("orderSearchERROR. ERROR "+ number);
}

// *********************************************************************************************************************
function findDistance(mode) {

    let  distance  = document.getElementById("querydistance").value;

    let data = JSON.stringify( {"distance": distance} );

    xhRequest2(true, "/entities/distance/find/"+mode, findDistanceOK, findDistanceERROR, data);

} // findDistance
function findDistanceOK(response) {
    let divBody = document.getElementById("queryBody");
    divBody.innerHTML = response;
}
function findDistanceERROR(name) {
    alert("findDistance. ERROR "+ name);
}

// *********************************************************************************************************************
function saveDistance() {

    let id       = document.getElementById("id").value;
    let distance = document.getElementById("distance").value;
    let city0    = document.getElementById("city0").value;
    let city1    = document.getElementById("city1").value;

    let data = JSON.stringify( { "id": id, "distance": distance, "city0": city0, "city1": city1 } );

    xhRequest2(true, "/entities/distance/save/", saveDistanceOK, saveDistanceERROR, data);

} // saveDistance
function saveDistanceOK(response) {
    let id = document.getElementById("id");
    id.value = xhr.responseText;
}
function saveDistanceERROR(name) {
    alert("saveDistance. ERROR "+ name);
}

// *********************************************************************************************************************

// From distance.html
function getDistanceCity0() {
    let id = document.getElementById("city0").value;
    xhRequest("/queries/city/load/"+id, getDistanceCity0OK, getDistanceCity0ERROR);
} // getDistanceCity0
function getDistanceCity0OK(response) {
    let city = JSON.parse(response);
    document.getElementById("cityname0").value = city.name;
}
function getDistanceCity0ERROR(number) {
    alert("getDistanceCity0ERROR. ERROR "+ number);
}

// *********************************************************************************************************************

// From distance.html
function getDistanceCity1() {
    let id = document.getElementById("city1").value;
    xhRequest("/queries/city/load/"+id, getDistanceCity1OK, getDistanceCity1ERROR);
} // getDistanceCity1
function getDistanceCity1OK(response) {
    let city = JSON.parse(response);
    document.getElementById("cityname1").value = city.name;
}
function getDistanceCity1ERROR(number) {
    alert("getDistanceCity1ERROR. ERROR "+ number);
}