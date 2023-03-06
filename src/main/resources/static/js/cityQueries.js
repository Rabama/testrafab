

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
        if ("createEvent" in document) {
            let evt = document.createEvent("HTMLEvents");
            evt.initEvent("change", false, true);
//            document.getElementById("select").dispatchEvent(evt);
            fld.dispatchEvent(evt);
        } else fld.fireEvent("onchange");

    } else {
        alert("No se ha encontrado el campo del formulario " + campo);
    }
}
*/

// Desde
// Desde cityquerybody
function loadCity(id) {

    let xhr = new XMLHttpRequest();
    let url = "/entities/city/"+id; // CityController
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let divMain = document.getElementById("principal");
            divMain.innerHTML = xhr.responseText;
        }
    };

    xhr.send();
    return false;
} // loadCity

function findCity(mode) {

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let xhr = new XMLHttpRequest();
    let url = "/findCity/"+mode; // CityController
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let divBody = document.getElementById("queryBody");
            divBody.innerHTML = xhr.responseText;
        }
    };

    xhr.setRequestHeader(header, token);

//    let id = document.getElementById("queryid").value;
    let name = document.getElementById("queryname").value;
//    let data = JSON.stringify( {"id": id, "name": name} );
    let data = JSON.stringify( {"name": name} );
    xhr.send(data);
    return false;
} // findCity

function saveCity() {

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let xhr = new XMLHttpRequest();
    let url = "/saveCity";
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            switch (xhr.status) {
                case 200: {
                    id = document.getElementById("id");
                    id.value = xhr.responseText;
                    break;
                }
                case 400: { // BAD_REQUEST
                    alert("zERROR: " + xhr.responseText);
                    break;
                }
                default: {
                    alert("zERROR ("+xhr.status+"): "+xhr.responseText);
                }
            }
        }
    };

    xhr.setRequestHeader(header, token);

    let id = document.getElementById("id").value;
    let name = document.getElementById("name").value;
    let data = JSON.stringify( {"id": id, "name": name} );
    xhr.send(data);
    return false;
} // saveCity
