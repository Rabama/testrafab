
function xhRequest(url, resultOK, resultERROR, bundle) {
    let xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            switch (xhr.status) {
                case 200: {
                    resultOK(xhr.responseText, bundle);
                    break;
                }
                case 400: { // BAD_REQUEST
                    resultERROR(xhr.status);
                    break;
                }
                case 403: { // Acceso a la URL prohibido.
                    resultERROR(xhr.status);
                    break;
                }
                default: {
                    resultERROR(xhr.status);
                }
            }
        }
    };
    xhr.send(null);
} // xhRequest


function loadDiv(path, divId) {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if ((xhr.readyState === 4) && (xhr.status === 200)) {
            document.getElementById(divId).innerHTML = xhr.responseText;
        }
    }
    xhr.open("GET", path, true);
    xhr.send();
} // loadDiv



function queryHead(origenId, origenFd, destinoId, campoFrm, recurso, panelId) {
//    alert("queryHead");
    let bundle = JSON.stringify( {"origenId": origenId, "origenFd": origenFd, "destinoId": destinoId, "campoFrm": campoFrm, "recurso": recurso, "panelId": panelId} );
    xhRequest(recurso, queryHeadOK, queryHeadERROR, bundle);
} // queryHead
function queryHeadOK(response, bundle) {
//    alert("queryHeadOK: "+ bundle);
    let data = JSON.parse(bundle);

    document.getElementById(data.panelId).innerHTML = response;

    document.getElementById("valor").value     = document.getElementById(data.origenId).value;
    document.getElementById("origenFd").value  = data.origenFd;
    document.getElementById("destinoId").value = data.destinoId;

    document.getElementById(data.campoFrm).value = document.getElementById(data.origenId).value;

    let btn = document.getElementById("search")
    let evt = document.createEvent("HTMLEvents");
    evt.initEvent("click", false, true);
    btn.dispatchEvent(evt);
}
function queryHeadERROR(number) {
    alert("queryHeadERROR. ERROR "+ number);
}

// Opción 1: Actualiamos los campos.
// Opción 2: Actualimos el panel.
function querySelectById(valor) {
    alert("querySelectById.destinoId "+document.getElementById("destinoId").value+" con valor "+valor);

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



// Desde truck.html
function getTruckCity() {
    let value = document.getElementById("cityid").value;
    let xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", "/city/"+value, false );
    xmlHttp.send( null );
    let response = JSON.parse(xmlHttp.responseText);
    document.getElementById("cityname").value = response.name;
} // getTruckCity

// Desde cargo.html
function getCargoUpCity() {
    alert("getCargoUpCity " + document.getElementById("upcity").value);
    let value = document.getElementById("upcity").value;
    let xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", "/city/"+value, false );
    xmlHttp.send( null );
    let response = JSON.parse(xmlHttp.responseText);
    document.getElementById("upcityname").value = response.name;
} // getCargoUpCity

// Desde cargo.html
function getCargoUnCity() {
    alert("getCargoUnCity " + document.getElementById("uncity").value);
    let value = document.getElementById("uncity").value;
    let xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", "/city/"+value, false );
    xmlHttp.send( null );
    let response = JSON.parse(xmlHttp.responseText);
    document.getElementById("uncityname").value = response.name;
} // getCargoUnCity


function getOrderTruck() {
    let url = "/truck/" + document.getElementById("truckid").value;
    xhRequest(url, getOrderTruckOK, getOrderTruckERROR);
}
function getOrderTruckOK(response) {
    document.getElementById("trucknumber").value = JSON.parse(response).number;
}
function getOrderTruckERROR(number) {
    alert("getOrderTruck. ERROR "+ number);
}

function getOrderDriver1() {
    let url = "/driver/" + document.getElementById("driver1id").value;
    xhRequest(url, getOrderDriver1OK, getOrderDriver1ERROR);
}
function getOrderDriver1OK(response) {
    document.getElementById("driver1name").value = JSON.parse(response).name;
}
function getOrderDriver1ERROR(name) {
    alert("getOrderDriver1. ERROR "+ name);
}

function getOrderDriver2() {
    let url = "/driver/" + document.getElementById("driver2id").value;
    xhRequest(url, getOrderDriver2OK, getOrderDriver2ERROR);
}
function getOrderDriver2OK(response) {
    document.getElementById("driver2name").value = JSON.parse(response).name;
}
function getOrderDriver2ERROR(name) {
    alert("getOrderDriver2. ERROR "+ name);
}

function getOrderDriver3() {
    let url = "/driver/" + document.getElementById("driver3id").value;
    xhRequest(url, getOrderDriver3OK, getOrderDriver3ERROR);
}
function getOrderDriver3OK(response) {
    document.getElementById("driver3name").value = JSON.parse(response).name;
}
function getOrderDriver3ERROR(name) {
    alert("getOrderDriver3. ERROR "+ name);
}
