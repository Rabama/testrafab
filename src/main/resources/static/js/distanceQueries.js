function loadDistance(id) {

    let xhr = new XMLHttpRequest();
    let url = "/entities/distance/"+id; // TruckController
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let divMain = document.getElementById("principal");
            divMain.innerHTML = xhr.responseText;
        }
    };

    xhr.send();
    return false;
} // loadDistance

function findDistance(mode) {

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let xhr = new XMLHttpRequest();
    let url = "/findDistance/"+mode; // TruckController
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

    let data = JSON.stringify( {"number": number, "status": status} ); alert(data);
    xhr.send(data);
    return false;
} // findTruck

function saveDistance() {
    alert("saveDistance() 1");

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let xhr = new XMLHttpRequest();
    let url = "/saveDistance";
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
                    alert("distanceERROR: " + xhr.responseText);
                    break;
                }
                default: {
                    alert("distanceERROR ("+xhr.status+"): "+xhr.responseText);
                }
            }
        }
    };

    xhr.setRequestHeader(header, token);

    let id       = document.getElementById("id").value;
    let distance  = document.getElementById("distance").value;
    let time = document.getElementById("time").value;
    let city0   = document.getElementById("city0").value;
    let city1     = document.getElementById("city1").value;

    let data = JSON.stringify( {"id": id, "distance": distance, "time": time, "city0": city0, "city1": city1} );
    xhr.send(data);
    alert("saveDistance 2");
    return false;
} // saveTruck
