function prueba(recurso, elementoId) {
    let asd = new XMLHttpRequest();
    asd.onreadystatechange = function () {
        if ((asd.readyState === 4) && (asd.status === 200)) {
            document.getElementById(elementoId).innerHTML = asd.responseText;
        }
    }
    alert("Recurso = " + recurso);
    asd.open("GET", recurso, true);
    asd.send();
} // prueba

function httpRequest(recurso, igual, tabla, campo, elementoId) {
    let asd = new XMLHttpRequest();
    asd.onreadystatechange = function () {
        if ((asd.readyState === 4) && (asd.status === 200)) {
            document.getElementById(elementoId).innerHTML = asd.responseText;
        }
    }
    let valor = document.getElementById(campo).value;
    if (igual === true) { recurso += "/equal" } else { recurso += "/like"}
    recurso += "/"+tabla+"/"+campo+"/"+valor;
    asd.open("GET", recurso, true);
    asd.send();
} // prueba
