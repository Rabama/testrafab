


// Desde cargoquerybody
function loadCargo(id) {

    let xhr = new XMLHttpRequest();
    let url = "/entities/cargo/"+id; // CargoController
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let divMain = document.getElementById("principal");
            divMain.innerHTML = xhr.responseText;
        }
    };

    xhr.send();
    return false;
} // loadCargo

function findCargo(mode) {

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let xhr = new XMLHttpRequest();
    let url = "/findCargo/"+mode; // CargoController
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            switch (xhr.status) {
                case 200: {
                    let divBody = document.getElementById("queryBody");
                    divBody.innerHTML = xhr.responseText;
                    break;
                }
                default: {
                    alert("findCargo. ERROR " + xhr.status);
                }
            }
        }
    };

    xhr.setRequestHeader(header, token);

    let name = document.getElementById("queryname").value;
    let status = document.getElementById("querystatus").value;

    let data = JSON.stringify( {"name": name, "status": status} );
//    alert("JSON: "+data);
    xhr.send(data);
    return false;
} // findCargo

function saveCargo() {
//    alert("saveCargo() 1");

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let xhr = new XMLHttpRequest();
    let url = "/saveCargo";
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let id = document.getElementById("id");
            id.value = xhr.responseText;
        }
    };

    xhr.setRequestHeader(header, token);

    let id     = document.getElementById("id").value;
    let name   = document.getElementById("name").value;
    let weight = document.getElementById("weight").value;
    let status = document.getElementById("status").value;
    let upcity = document.getElementById("upcity").value;
    let uncity = document.getElementById("uncity").value;

    let data = JSON.stringify( {"id": id, "name": name, "weight": weight, "status": status, "upcity": upcity, "uncity": uncity} );
    xhr.send(data);
//    alert("saveCargo() 2");
    return false;
} // saveCargo
