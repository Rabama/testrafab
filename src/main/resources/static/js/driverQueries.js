// Desde
// Desde truckquerybody
function loadDriver(id) {

    let xhr = new XMLHttpRequest();
    let url = "/entities/driver/"+id; // TruckController
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let divMain = document.getElementById("principal");
            divMain.innerHTML = xhr.responseText;
        }
    };

    xhr.send();
    return false;
} // loadTruck

function findDriver(mode) {

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let xhr = new XMLHttpRequest();
    let url = "/findDriver/"+mode; // TruckController
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let divBody = document.getElementById("queryBody");
            divBody.innerHTML = xhr.responseText;
        }
    };

    xhr.setRequestHeader(header, token);

    let name   = document.getElementById("queryname").value;
    let status = document.getElementById("querystatus").value;

    let data = JSON.stringify( {"name": name, "status": status} );
//    alert(data);
    xhr.send(data);
    return false;
} // findTruck

function saveDriver() {
//    alert("saveDriver() 1");

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let xhr = new XMLHttpRequest();
    let url = "/saveDriver";
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let id = document.getElementById("id");
            id.value = xhr.responseText;
        }
    };

    xhr.setRequestHeader(header, token);

    let id      = document.getElementById("id").value;
    let name    = document.getElementById("name").value;
    let surname = document.getElementById("surname").value;
    let workedh = document.getElementById("workedhours").value;
    let status  = document.getElementById("status").value;

    let data = JSON.stringify( {"id": id, "name": name, "surname": surname, "workedhours": workedh, "status": status} );
    xhr.send(data);
//    alert("saveDriver() 2");
    return false;
} // saveDriver
