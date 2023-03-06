

/*
function queryHead(origenId, origenFd, destinoId, campoFrm, recurso, panelId) {

    const request = new XMLHttpRequest();
    request.onreadystatechange = function () {
        if ((request.readyState === 4) && (request.status === 200)) {

            document.getElementById(panelId).innerHTML = request.responseText;
            document.getElementById("valor").value     = document.getElementById(origenId).value;
            document.getElementById("origenFd").value  = origenFd;
            document.getElementById("destinoId").value = destinoId;
            document.getElementById(campoFrm).value = document.getElementById(origenId).value;

            let btn = document.getElementById("search")
            if ("createEvent" in document) {
                let evt = document.createEvent("HTMLEvents");
                evt.initEvent("click", false, true);
                btn.dispatchEvent(evt);
            } else btn.fireEvent("onclick");

        }
    }

    request.open('GET', recurso, true);
    request.send();
} // queryHead

// Opción 1: Actualiamos los campos.
// Opción 2: Actualimos el panel.
function querySelectById(valor) {
    let campo = document.getElementById("destinoId").value;
    let fld = document.getElementById(campo);
    if (fld != null) {
        fld.value = valor; // Asignamos el valor
        fld.blur(); // Perdemos el foco

        //
//        if ("createEvent" in document) {
            let evt = document.createEvent("HTMLEvents");
            evt.initEvent("change", false, true);
//            document.getElementById("select").dispatchEvent(evt);
            fld.dispatchEvent(evt);
//        } else fld.fireEvent("onchange");

    } else {
        alert("No se ha encontrado el campo del formulario " + campo);
    }
}
*/

// Desde
// Desde truckquerybody
function loadTruck(id) {

    let xhr = new XMLHttpRequest();
    let url = "/entities/truck/"+id; // TruckController
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

function findTruck(mode) {

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let xhr = new XMLHttpRequest();
    let url = "/findTruck/"+mode; // TruckController
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let divBody = document.getElementById("queryBody");
            divBody.innerHTML = xhr.responseText;
        }
    };

    xhr.setRequestHeader(header, token);

    let number = document.getElementById("querynumber").value;
    let status = document.getElementById("querystatus").value;

    let data = JSON.stringify( {"number": number, "status": status} );
//    alert(data);
    xhr.send(data);
    return false;
} // findTruck

function saveTruck() {
//    alert("saveTruck() 1");

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let xhr = new XMLHttpRequest();
    let url = "/saveTruck";
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let id = document.getElementById("id");
            id.value = xhr.responseText;
        }
    };

    xhr.setRequestHeader(header, token);

    let id       = document.getElementById("id").value;
    let number   = document.getElementById("number").value;
    let capacity = document.getElementById("capacity").value;
    let status   = document.getElementById("status").value;
    let cityId     = document.getElementById("cityid").value;

    let data = JSON.stringify( {"id": id, "number": number, "capacity": capacity, "status": status, "cityId": cityId} );
    xhr.send(data);
//    alert("saveTruck() 2");
    return false;
} // saveTruck
