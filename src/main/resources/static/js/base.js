
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
                    resultERROR(xhr.status + ": " + xhr.responseText);
                    break;
                }
                case 403: { // Acceso a la URL prohibido.
                    resultERROR(xhr.status + ": " + xhr.responseText);
                    break;
                }
                default: {
                    resultERROR(xhr.status + ": " + xhr.responseText);
                }
            }
        }
    };
    xhr.send(null);
} // xhRequest

// *********************************************************************************************************************
function xhRequest2(methodPost, url, resultOK, resultERROR, bundle) {
    let token = "";
    let header = "";

    let xhr = new XMLHttpRequest();

    if (methodPost) {
        token = document.querySelector('meta[name="_csrf"]').content;
        header = document.querySelector('meta[name="_csrf_header"]').content;
        xhr.open("POST", url, true);
        xhr.setRequestHeader("Content-Type", "application/json");
    } else {
        xhr.open("GET", url, true);
    }

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            switch (xhr.status) {
                case 200:
                    if (xhr.responseText.substring(0, 5) === "Error") {
                        resultERROR(xhr.responseText);
                    } else {
                        resultOK(xhr.responseText, bundle);
                    }
                    break;

                case 400:  // BAD_REQUEST
                    resultERROR(xhr.status + ": " + xhr.responseText);
                    break;

                case 403:  // Acceso prohibido a la URL.
                    resultERROR(xhr.status + ": " + xhr.responseText);
                    break;

                default:
                    resultERROR(xhr.status + ": " + xhr.responseText);

            }
        }
    };
    if (methodPost) {
        xhr.setRequestHeader(header, token);
        xhr.send(bundle);
    } else {
        xhr.send(null);
    }
} // xhRequest2

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

// Opción 1: Actualizamos los campos.
// Opción 2: Actualizamos el panel.
function querySelectById(valor) {
//    alert("querySelectById.destinoId "+document.getElementById("destinoId").value+" con valor "+valor);

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




