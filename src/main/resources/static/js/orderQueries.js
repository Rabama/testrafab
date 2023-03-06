// import { xhRequest } from './base.js';


// Desde orderquerybody
function loadOrder(id) {
//    alert("loadOrder");
    let xhr = new XMLHttpRequest();
    let url = "/entities/order/"+id; // OrderController
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let divMain = document.getElementById("principal");
            divMain.innerHTML = xhr.responseText;

            //
            weightTotalCalculation();

        }
    };

    xhr.send();
    return false;
} // loadOrder

function findOrder(mode) {

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let xhr = new XMLHttpRequest();
    let url = "/findOrder/"+mode; // OrderController
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let divBody = document.getElementById("queryBody");
            divBody.innerHTML = xhr.responseText;
        }
    };

    xhr.setRequestHeader(header, token);

    let id = document.getElementById("queryid").value;
    let completed = document.getElementById("querycompleted").value;

    let data = JSON.stringify( {"id": id, "completed": completed} );
//    alert("JSON: "+data);
    xhr.send(data);
    return false;
} // findOrder

// function saveOrder() {
//     alert("saveOrder() 1");
//
//     let token = document.querySelector('meta[name="_csrf"]').content;
//     let header = document.querySelector('meta[name="_csrf_header"]').content;
//
//     let xhr = new XMLHttpRequest();
//     let url = "/saveOrder";
//     xhr.open("POST", url, true);
//     xhr.setRequestHeader("Content-Type", "application/json");
//     xhr.onreadystatechange = function () {
//         if (xhr.readyState === 4 && xhr.status === 200) {
//             let id = document.getElementById("id");
//             id.value = xhr.responseText;
//         }
//     };
//
//     xhr.setRequestHeader(header, token);
//
//     let id        = document.getElementById("id").value;
//     let completed = document.getElementById("completed").value;
//     let truckId   = document.getElementById("truckid").value;
//
//     let driver1Id = document.getElementById("driver1id").value;
//     let driver2Id = document.getElementById("driver2id").value;
//     let driver3Id = document.getElementById("driver3id").value;
//
//     let data = JSON.stringify( {"id": id, "completed": completed, "truckId": truckId,
//         "driver1Id": driver1Id, "driver2Id": driver2Id, "driver3Id": driver3Id} );
//     xhr.send(data);
//     alert("saveOrder() 2");
//     return false;
// } // saveOrder

function saveOrder() {
//    alert("saveOrder() 1");

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let xhr = new XMLHttpRequest();
    let url = "/saveOrder";
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            switch (xhr.status) {
                case 200: {
                    let id = document.getElementById("id");
                    id.value = xhr.responseText;
                    break;
                }
                case 400: { // BAD_REQUEST
                    alert("ERROR: " + xhr.responseText);
                    break;
                }
                default: {
                    alert("ERROR ("+xhr.status+"): "+xhr.responseText);
                }
            }
        }
    };

    xhr.setRequestHeader(header, token);

    // Base
    let id        = document.getElementById("id").value;
    let completed = document.getElementById("completed").value;
    let truckId   = document.getElementById("truckid").value;

    // Drivers
    let driver1Id = document.getElementById("driver1id").value;
    let driver2Id = document.getElementById("driver2id").value;
    let driver3Id = document.getElementById("driver3id").value;

    //  Cargos
    let table    = document.getElementById("cargoTable");
    let rowsNum  = table.rows.length;

    let baseJson = {"id": id, "completed": completed, "truckId": truckId, "driver1Id": driver1Id,
        "driver2Id": driver2Id, "driver3Id": driver3Id, "rowsNum": rowsNum};

    let cargoArray = [];
    for (let i=1; i<rowsNum; i++) {
        cargoArray.push(table.rows[i].cells[0].innerHTML); // id
        cargoArray.push(table.rows[i].cells[6].innerHTML); // upload
    }
    let cargoJson = Object.assign({}, cargoArray);

    //
    let asd = { ...baseJson, ...cargoJson };

    // Bundle
    let data = JSON.stringify( asd );

    alert(data);

    xhr.send(data);
//    alert("saveOrder() 2");
    return false;
} // saveOrder
function saveOrderOK() {

} // saveOrderOK
function saveOrderERROR() {

} // saveOrderERROR







function orderCargoAppendPRE() {
//    alert("queryHead");
    let recurso = '/cargo/query/head/1';
    xhRequest(recurso, orderCargoAppendPRE2, orderCargoAppendERROR);
} // queryHead

function orderCargoAppendERROR(number) {
    alert("queryHeadERROR. ERROR "+ number);
}

// Head de consulta cargado.
function orderCargoAppendPRE2(response) {
    document.getElementById('consultas').innerHTML = response;
    document.getElementById('destinoId').value = 'cargoid';
}

// Buscamos el cargo por su id.
function orderCargoAppendPRE3() {
    let recurso = '/cargo/order/'+document.getElementById('cargoid').value;
    xhRequest(recurso, orderCargoAppend, orderCargoAppendERROR);
}

function orderCargoAppend(bundle) {

    let data = JSON.parse(bundle);

    let table    = document.getElementById("cargoTable");
    let rowsNum  = table.rows.length;
    let cellsNum = table.rows[0].cells.length;
    let cellOptions = cellsNum-1;

    let rowUpload = table.insertRow(rowsNum);

    let asd;
    for (let i=0; i<cellsNum; i++) {
        asd = rowUpload.insertCell(i);
    }

    table.rows[rowsNum].cells[0].innerHTML = data.id;
    table.rows[rowsNum].cells[1].innerHTML = data.name;
    table.rows[rowsNum].cells[2].innerHTML = data.upcity;
    table.rows[rowsNum].cells[3].innerHTML = data.uncity;
    table.rows[rowsNum].cells[4].innerHTML = data.weight;
    table.rows[rowsNum].cells[5].innerHTML = '---';
    table.rows[rowsNum].cells[6].innerHTML = 'UPLOAD';
    table.rows[rowsNum].cells[7].innerHTML = '';
//    table.rows[rowsNum].cells[0].innerHTML = rowsNum.toString();

    asd.innerHTML =
        "<input type=\"button\" value=\"Subir\"  onclick=\"orderCargoRowUp("+rowsNum+");\"/>"+
        "<input type=\"button\" value=\"Bajar\"  onclick=\"orderCargoRowDown("+rowsNum+");\"/>"+
        "<input type=\"button\" value=\"Borrar\" onclick=\"orderCargoRowDelete("+rowsNum+");\"/>";

    rowsNum++;

    let rowUnload = table.insertRow(rowsNum);

//    let asd;
    for (let i=0; i<cellsNum; i++) {
        asd = rowUnload.insertCell(i);
    }

    table.rows[rowsNum].cells[0].innerHTML = data.id;
    table.rows[rowsNum].cells[1].innerHTML = data.name;
    table.rows[rowsNum].cells[2].innerHTML = data.upcity;
    table.rows[rowsNum].cells[3].innerHTML = data.uncity;
    table.rows[rowsNum].cells[4].innerHTML = data.weight;
    table.rows[rowsNum].cells[5].innerHTML = '---';
    table.rows[rowsNum].cells[6].innerHTML = '';
    table.rows[rowsNum].cells[7].innerHTML = 'UNLOAD';
//    table.rows[rowsNum].cells[0].innerHTML = rowsNum.toString();

    asd.innerHTML =
        "<input type=\"button\" value=\"Subir\"  onclick=\"orderCargoRowUp("+rowsNum+");\"/>"+
        "<input type=\"button\" value=\"Bajar\"  onclick=\"orderCargoRowDown("+rowsNum+");\"/>"+
        "<input type=\"button\" value=\"Borrar\" onclick=\"orderCargoRowDelete("+rowsNum+");\"/>";



//    alert("Row appended"+"\nrowsNum: "+rowsNum+"\ncellsNum: "+cellsNum+"\ncellOptions: "+cellOptions);
}

function orderCargoRowUp(target) {
    if (target <= 1) return;

//	alert("rowUp " + target);

    let table = document.getElementById("cargoTable");
    let cells = table.rows[0].cells;

    let row = table.insertRow(target-1);
    for (let i=0; i<cells.length; i++) {
        row.insertCell(i);
    }

    let newRow = target-1;
    // Aquí está la que nos saltamos.
    let oldRow = target+1;

    let oldCells = table.rows[oldRow].cells;
    let newCells = table.rows[newRow].cells;

    // No podemos cambiar los enlaces de la última columna.
    for (let j=0; j<cells.length-1; j++) {
        newCells[j].innerHTML = oldCells[j].innerHTML;
    }

    for (let k=newRow; k<oldRow; k++) {
        newCells = table.rows[k].cells;
        oldCells = table.rows[k+1].cells;
        newCells[cells.length-1].innerHTML = oldCells[cells.length-1].innerHTML
    }

    table.deleteRow(target+1);

    //
    weightTotalCalculation();

} // orderCargoRowUp


function orderCargoRowDown(target) {

//	alert("rowDown " + target);

    let table = document.getElementById("cargoTable");
    let rowsNum  = table.rows.length;

    if (target === (rowsNum-1)) return;

    orderCargoRowUp(target+1);

} // orderCargoRowDown

function orderCargoRowDelete(actual) {

} // orderCargoRowDelete

function weightTotalCalculation() {

    let table    = document.getElementById("cargoTable");
    let rowsNum  = table.rows.length;

    let totalWeight = 0.0;
    for (let i=1; i<rowsNum; i++) {
        let partialWeight = 0.0;
        try {
            partialWeight = parseFloat(table.rows[i].cells[4].innerHTML);
            if (table.rows[i].cells[6].innerHTML === "") {
                partialWeight *= -1;
            }
        } catch (error) {
            //
        }
        totalWeight += partialWeight;
        table.rows[i].cells[5].innerHTML = totalWeight.toString();
    }

} // weightTotalCalculation
