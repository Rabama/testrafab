
const otId         =  0;
const otName       =  1;
const otUpCityId   =  2;
const otUnCityId   =  3;
const otUpCityName =  4;
const otUnCityName =  5;
const otUpload     =  6;
const otUnload     =  7;
const otWeight     =  8;
const otTotal      =  9;
const otHours      = 10;
const otStatus     = 11;

// *********************************************************************************************************************
// Desde order
function newOrder() {
    return loadOrder(-1);
}

function delOrder() {

    let id       = document.getElementById("id").value;

    let data = JSON.stringify( { "id": id} );

    xhRequest2(true, "/entities/order/delete/", delOrderOK, delOrderERROR, data);

} // saveDistance
function delOrderOK(response) {
    newOrder();
}
function delOrderERROR(name) {
    alert("delOrder. ERROR "+ name);
}


// *********************************************************************************************************************
// Desde orderquerybody
function loadOrder(id) {
//    alert("loadOrder1");
    let xhr = new XMLHttpRequest();
    let url = "/entities/order/"+id; // OrderController
//    alert("loadOrder2");
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let divMain = document.getElementById("principal");
            divMain.innerHTML = xhr.responseText;

            //weightTotalCalculation();


            orderWeight();
            orderHours();

        }
    };

    xhr.send();
    return false;
} // loadOrder

function loadOrderERROR(number) {
    alert("loadOrder. ERROR "+ number);
}

// *********************************************************************************************************************
function findOrder(mode) {
    let id        = document.getElementById("queryid").value;
    let completed = document.getElementById("querycompleted").value;

    let data = JSON.stringify( { "id": id, "completed": completed } );

    xhRequest2(true, '/entities/order/find/'+mode, findOrderOK, findOrderERROR, data);
} // findOrder
function findOrderOK(response) {
    let divBody = document.getElementById("queryBody");
    divBody.innerHTML = response;
}
function findOrderERROR(number) {
    alert("findOrder. ERROR "+ name);
}


// *********************************************************************************************************************
function saveOrder() {
//    alert("saveOrder() 1");

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

    // Text field.
    let baseJson = {"id": id, "completed": completed, "truckId": truckId, "rowsNum": rowsNum,
        "driver1Id": driver1Id, "driver2Id": driver2Id, "driver3Id": driver3Id };

    // Numeric field.
    let cargoArray = [];
    for (let i=1; i<rowsNum; i++) {
        cargoArray.push(table.rows[i].cells[otId].innerHTML);     // id
        cargoArray.push(table.rows[i].cells[otUpload].innerHTML); // upload
    }
    let cargoJson = Object.assign({}, cargoArray);

    //
    let asd = { ...baseJson, ...cargoJson };

    // Bundle
    let data = JSON.stringify( asd );

//    alert(data);

    xhRequest2(true, "/entities/order/save/", saveOrderOK, saveOrderERROR, data);
} // saveOrder
function saveOrderOK(response) {
    // let id = document.getElementById("id");
    // id.value = response;

    loadOrder(response);

} // saveOrderOK
function saveOrderERROR(number) {
    alert("saveOrderERROR. " + number)
} // saveOrderERROR


// *********************************************************************************************************************
function orderSearch() {
    xhRequest2(false, '/queries/order/head/0', orderSearchOK, orderSearchERROR);
}
function orderSearchOK(response) {
    document.getElementById('consultas').innerHTML = response;
    document.getElementById('destinoId').value = 'id';

    document.getElementById('queryid').value        = document.getElementById('id').value;
    document.getElementById('querycompleted').value = document.getElementById('completed').value;
}
function orderSearchERROR(number) {
    alert("orderSearchERROR. ERROR "+ number);
}

// *********************************************************************************************************************
function orderTruck() {
//    alert("orderCargoAppendPRE");
    xhRequest('/queries/truck/head/1', orderTruckOK, orderTruckERROR);
}
function orderTruckOK(response) {
    document.getElementById('consultas').innerHTML = response;
    document.getElementById('destinoId').value = 'truckid';

    document.getElementById('querycityid').value   = document.getElementById('workcityid').value;
    document.getElementById('querycityname').value = document.getElementById('workcityname').value;
}
function orderTruckERROR(response) {
    alert("queryHeadERROR. ERROR "+ number);
}

// *********************************************************************************************************************
function orderCargoAppendPRE() {
//    alert("orderCargoAppendPRE");
    let recurso = '/queries/cargo/head/1';
    xhRequest(recurso, orderCargoAppendPRE2, orderCargoAppendERROR);
} // queryHead

function orderCargoAppendERROR(number) {
    alert("queryHeadERROR. ERROR "+ number);
}

// Head de consulta cargado.
function orderCargoAppendPRE2(response) {
//    alert("orderCargoAppendPRE2");
    document.getElementById('consultas').innerHTML = response;
    document.getElementById('destinoId').value = 'cargoid';

    document.getElementById('queryupcityid').value   = document.getElementById('upcityid').value;
    document.getElementById('queryupcityname').value = document.getElementById('upcityname').value;
    document.getElementById('queryuncityid').value   = document.getElementById('uncityid').value;
    document.getElementById('queryuncityname').value = document.getElementById('uncityname').value;

}

// Buscamos el cargo por su id.
// *********************************************************************************************************************
function orderCargoAppendPRE3() {
//    alert("orderCargoAppendPRE3 "+document.getElementById('cargoid').value);
    let recurso = '/queries/cargo/load/'+document.getElementById('cargoid').value;
    xhRequest(recurso, orderCargoAppend, orderCargoAppendERROR);
}

// *********************************************************************************************************************
function orderCargoNewRow(bundle, upload) {
//    alert("orderCargoNewRow");

    let data = JSON.parse(bundle);

    let table    = document.getElementById("cargoTable");
    let cellsNum = table.rows[0].cells.length; // Existe una fila.
    let rowsNum  = table.rows.length;
    let newRow   = table.insertRow(rowsNum);

    let asd;
    for (let i=0; i<cellsNum; i++) {
        asd = newRow.insertCell(i);
    }

    table.rows[rowsNum].cells[otId].innerHTML         = data.id;
    table.rows[rowsNum].cells[otName].innerHTML       = data.name;
    table.rows[rowsNum].cells[otUpCityId].innerHTML   = data.upCityId;
    table.rows[rowsNum].cells[otUnCityId].innerHTML   = data.unCityId;
    table.rows[rowsNum].cells[otUpCityName].innerHTML = data.upCityName;
    table.rows[rowsNum].cells[otUnCityName].innerHTML = data.unCityName;
    if ( upload) table.rows[rowsNum].cells[otUpload].innerHTML = 'UPLOAD';
    if (!upload) table.rows[rowsNum].cells[otUnload].innerHTML = 'UNLOAD';
    table.rows[rowsNum].cells[otWeight].innerHTML     = data.weight;
    table.rows[rowsNum].cells[otTotal].innerHTML      = '---';
    table.rows[rowsNum].cells[otHours].innerHTML      = data.hours;
    table.rows[rowsNum].cells[otStatus].innerHTML     = data.status;

    asd.innerHTML =
        "<input type=\"button\" value=\"Subir\"  onclick=\"orderCargoRowUp("+rowsNum+");\"/>"+
        "<input type=\"button\" value=\"Bajar\"  onclick=\"orderCargoRowDown("+rowsNum+");\"/>"+
        "<input type=\"button\" value=\"Borrar\" onclick=\"orderCargoRowDelete("+rowsNum+");\"/>";
}

// *********************************************************************************************************************
function orderCargoAppend(bundle) {
//    alert("orderCargoAppend");


    orderCargoNewRow(bundle, true);
    orderCargoNewRow(bundle, false);

    orderTableHours();

    // Cálculo del peso
    orderWeight();

    // Cálculo de las horas
    orderHours();


//    alert("Row appended"+"\nrowsNum: "+rowsNum+"\ncellsNum: "+cellsNum+"\ncellOptions: "+cellOptions);
}

// *********************************************************************************************************************
function orderWeight() {

    let table    = document.getElementById("cargoTable");
    let rowsNum  = table.rows.length;
    let weight = 0;
    let maxWeight = 0;
    for (let r=1; r<rowsNum; r++) {
        if (table.rows[r].cells[otUpload].innerHTML !== "") {
            weight += parseFloat(table.rows[r].cells[otWeight].innerHTML);
        } else {
            weight -= parseFloat(table.rows[r].cells[otWeight].innerHTML);
        }
        if (weight > maxWeight) {
            maxWeight = weight;
        }
        table.rows[r].cells[otTotal].innerHTML = weight.toString();
//        alert("Peso: " + peso + " / " + pesoMax);
    }
    document.getElementById('weight').value = maxWeight.toString();

}

// *********************************************************************************************************************
function orderHours() {

    let table    = document.getElementById("cargoTable");
    let rowsNum  = table.rows.length;
    let hours = 0;
    for (let r=1; r<rowsNum; r++) {
        hours += parseFloat(table.rows[r].cells[4].innerHTML);
    }

    hours = Math.round(hours * 100) / 100;

    document.getElementById('hours').value = hours.toString();

}

// *********************************************************************************************************************
function orderTableHours() {

    let table    = document.getElementById("cargoTable");
    let rowsNum  = table.rows.length;
    for (let r=2; r<rowsNum; r++) {

        if ((table.rows[r].cells[otStatus].innerHTML === "READY") ||    // Nuevo
            (table.rows[r].cells[otStatus].innerHTML === "ASSIGNED") || // Antiguo
            ((table.rows[r].cells[otStatus].innerHTML === "SHIPPED")  && // Cargado
                (table.rows[r].cells[otUpload].innerHTML === ""))) {

            let prevCityId;
            if (table.rows[r - 1].cells[otUpload].innerHTML === 'UPLOAD') {
                prevCityId = table.rows[r - 1].cells[otUpCityId].innerHTML;
            } else {
                prevCityId = table.rows[r - 1].cells[otUnCityId].innerHTML;
            }

            let actCityId;
            if (table.rows[r].cells[otUpload].innerHTML === 'UPLOAD') {
                actCityId = table.rows[r].cells[otUpCityId].innerHTML;
            } else {
                actCityId = table.rows[r].cells[otUnCityId].innerHTML;
            }

            getOrderCargoHours(prevCityId, actCityId);
        }
    }

}

// *********************************************************************************************************************
function getorderHours(upcity, uncity) {
    xhRequest("/queries/orderhours/"+upcity+"/"+uncity, getorderHoursOK, getorderHoursERROR);
} //
function getorderHoursOK(response) {
    let hours = document.getElementById("hours");
    hours.value = response;
}
function getorderHoursERROR(name) {
    alert("getHours. ERROR "+ name);
}

// *********************************************************************************************************************
function getOrderCargoHours(upcity, uncity) {
//    alert("getOrderCargoHours " + upcity + "/ " + uncity);

    let baseJson = { "city1Id": upcity, "city2Id": uncity };

    let data = JSON.stringify(baseJson);

    xhRequest2(true, "/queries/orderhours/", getOrderCargoHoursOK, getOrderCargoHoursERROR, data);
}
function getOrderCargoHoursOK(bundle) {
    // alert("getOrderCargoHoursOK " + bundle);

    let data = JSON.parse(bundle);

    let city1 = data.City1Id;
    let city2 = data.City2Id;
    let hours = data.hours;

    // alert("city1 " + city1 + "\n" +
    //       "city2 " + city2 + "\n" +
    //       "hours " + hours);

    let table    = document.getElementById("cargoTable");
    let rowsNum  = table.rows.length;

    let total = 0.0;
    let lastCity = table.rows[1].cells[otUpCityId].innerHTML;
    table.rows[1].cells[otHours].innerHTML = "0";
    for (let i=2; i<rowsNum; i++) {

        let actualCity;
        let actualUpload = (table.rows[i].cells[otUpload].innerHTML !== '');

        if (actualUpload) {
            actualCity = table.rows[i].cells[otUpCityId].innerHTML;
        } else {
            actualCity = table.rows[i].cells[otUnCityId].innerHTML;
        }

        if ((table.rows[i].cells[otStatus].innerHTML === "READY") ||
            (table.rows[i].cells[otStatus].innerHTML === "ASSIGNED") ||
            ((table.rows[i].cells[otStatus].innerHTML === "SHIPPED")  &&
                (table.rows[i].cells[otUpload].innerHTML === ""))) {

            // if ((lastCity === city1) || (lastCity === city2)) {
            //     if ((actualCity === city1) || (actualCity === city2)) {

            if (lastCity === city1) {
                if (actualCity === city2) {

//                alert("Row (" + i + ") Last/actual/hours: " + lastCity + "/" + actualCity + "/" + hours);

                    table.rows[i].cells[otHours].innerHTML = hours;

                }
            }

        }

        total += parseFloat(table.rows[i].cells[otHours].innerHTML.replace(',', '.'));

        // Next iteration.
        lastCity = actualCity;
    } // for


    //
    let hoursTable = document.getElementById("hours");
    hoursTable.value = (Math.round(total * 100) / 100).toString();
}
function getOrderCargoHoursERROR(name) {
    alert("getHours. ERROR "+ name);
}



// *********************************************************************************************************************
function orderCargoCheckOperation(target, opDelete) {

    let table = document.getElementById("cargoTable");

    if (table.rows[target]  .cells[otStatus].innerHTML === "DELIVERED") {
        alert("The cargo is delivered");
        return false;
    }
    if (table.rows[target-1].cells[otStatus].innerHTML === "DELIVERED") {
        alert("The cargo is delivered");
        return false;
    }

    // Constraints.
    if (opDelete) {

        // We can't delete a shipped cargo.
        if (table.rows[target].cells[otStatus].innerHTML === "SHIPPED") {
            alert("The cargo is shipped");
            return false;
        }

    } else {

        if ((table.rows[target]  .cells[otStatus].innerHTML === "SHIPPED") &&
            (table.rows[target]  .cells[otUpload].innerHTML !== "")) {
            alert("The good is uploaded");
            return false;
        }
        if ((table.rows[target-1].cells[otStatus].innerHTML === "SHIPPED") &&
            (table.rows[target-1].cells[otUpload].innerHTML !== "")) {
            alert("The good is uploaded");
            return false;
        }

    }

    return true;
}

// *********************************************************************************************************************
function orderCargoRowUp(target) {
    if (target <= 1) return;

//	alert("rowUp " + target);

    let table = document.getElementById("cargoTable");
    let cells = table.rows[0].cells;

    // Constraints.
    if (!orderCargoCheckOperation(target)) return;

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
    orderTableHours();
    orderWeight();
    orderHours();

} // orderCargoRowUp


// *********************************************************************************************************************
function orderCargoRowDown(target) {

//	alert("rowDown " + target);

    let table = document.getElementById("cargoTable");
    let rowsNum  = table.rows.length;

    if (target === (rowsNum-1)) return;

    orderCargoRowUp(target+1);

} // orderCargoRowDown

// *********************************************************************************************************************
function orderCargoRowDelete(target) {

    // Constraints.
    if (!orderCargoCheckOperation(target, true)) return;

    let table = document.getElementById("cargoTable");
    let cargoId = table.rows[target].cells[otId].innerHTML;

    xhRequest2(false, "/queries/order/cargo/ready/"+cargoId, orderCargoRowDeleteOK, orderCargoRowDeleteERROR);

} // orderCargoRowDelete
function orderCargoRowDeleteOK(response) {
    alert("orderCargoRowDeleteOK: " + response);

    let table = document.getElementById("cargoTable");
    let cells = table.rows[0].cells;
    let cargoId = response;

    let rowsNum = table.rows.length;
    for (let i=1; i<rowsNum; i++) {
//        console.log("row processing " + i);
        if (table.rows[i].cells[otId].innerHTML === cargoId) {

            // Save the last cells.
            let aux1 = table.rows[i].cells[otOptions].innerHTML;
            for (let j=i+1; j<rowsNum; j++) {
                let aux2 = table.rows[j].cells[otOptions].innerHTML;
                table.rows[j].cells[otOptions].innerHTML = aux1;
                aux1 = aux2;
            }

            //
            table.deleteRow(i);
            i--;
            rowsNum--;
        }
    }

    //
    orderTableHours();
    orderWeight();
    orderHours();
}
function orderCargoRowDeleteERROR(number) {
    alert("orderCargoRowDeleteERROR " + number);
}



// *********************************************************************************************************************
function orderWorkCityClear() {
    document.getElementById('workcityid').value   = "";
    document.getElementById('workcityname').value = "";
}
// *********************************************************************************************************************
function orderTruckClear() {
    document.getElementById('truckid').value       = "";
    document.getElementById('trucknumber').value   = "";
    document.getElementById('truckcapacity').value = "";
}
// *********************************************************************************************************************
function orderDriverClear(num) {
    document.getElementById('driver'+num+'id').value    = "";
    document.getElementById('driver'+num+'name').value  = "";
    document.getElementById('driver'+num+'hours').value = "";
}
// *********************************************************************************************************************
function orderUpcityClear() {
    document.getElementById('upcityid').value   = "";
    document.getElementById('upcityname').value = "";
}
// *********************************************************************************************************************
function orderUncityClear() {
    document.getElementById('uncityid').value   = "";
    document.getElementById('uncityname').value = "";
}


// *********************************************************************************************************************

// From order.html
function getOrderTruck() {
    let id = document.getElementById("truckid").value;
    xhRequest("/queries/truck/load/"+id, getOrderTruckOK, getOrderTruckERROR);
} //getOrderTruck
function getOrderTruckOK(response) {
    let truck = JSON.parse(response);
    document.getElementById("trucknumber").value = truck.number;
    document.getElementById("truckcapacity").value = truck.capacity;
}
function getOrderTruckERROR(number) {
    alert("getOrderTruck. ERROR "+ number);
}

// *********************************************************************************************************************

// From order.html
function getOrderDriver1() {
    let id = document.getElementById("driver1id").value;
    xhRequest("/queries/driver/load/"+id, getOrderDriver1OK, getOrderDriver1ERROR);
} //getOrderDriver1
function getOrderDriver1OK(response) {
    let driver = JSON.parse(response);
    document.getElementById("driver1name").value = driver.name;
}
function getOrderDriver1ERROR(name) {
    alert("getOrderDriver1. ERROR "+ name);
}
// *********************************************************************************************************************

// From order.html
function getOrderDriver2() {
    let id = document.getElementById("driver2id").value;
    xhRequest("/queries/driver/load/"+id, getOrderDriver2OK, getOrderDriver2ERROR);
} //getOrderDriver2
function getOrderDriver2OK(response) {
    let driver = JSON.parse(response);
    document.getElementById("driver2name").value = driver.name;
}
function getOrderDriver2ERROR(name) {
    alert("getOrderDriver2. ERROR "+ name);
}

// *********************************************************************************************************************

// From order.html
function getOrderDriver3() {
    let id = document.getElementById("driver3id").value;
    xhRequest("/queries/driver/load/"+id, getOrderDriver3OK, getOrderDriver3ERROR);
} //getOrderDriver3
function getOrderDriver3OK(response) {
    let driver = JSON.parse(response);
    document.getElementById("driver3name").value = driver.name;
}
function getOrderDriver3ERROR(name) {
    alert("getOrderDriver3. ERROR "+ name);
}

// *********************************************************************************************************************

// From order.html
function getOrderWorkCity() {
    let id = document.getElementById("workcityid").value;
    xhRequest("/queries/city/load/"+id, getOrderWorkCityOK, getOrderWorkCityERROR);
} //getOrderWorkCity
function getOrderWorkCityOK(response) {
    let city = JSON.parse(response);
    document.getElementById("workcityname").value = city.name;
}
function getOrderWorkCityERROR(number) {
    alert("getOrderWorkCityERROR. ERROR "+ number);
}
// *********************************************************************************************************************

// From order.html
function getOrderUpcity() {
    let id = document.getElementById("upcityid").value;
    xhRequest("/queries/city/load/"+id, getOrderUpcityOK, getOrderUpcityERROR);
} //getOrderUpcity
function getOrderUpcityOK(response) {
    let city = JSON.parse(response);
    document.getElementById("upcityname").value = city.name;
}
function getOrderUpcityERROR(number) {
    alert("getOrderUpcityERROR. ERROR "+ number);
}
// *********************************************************************************************************************

// From order.html
function getOrderUncity() {
    let id = document.getElementById("uncityid").value;
    xhRequest("/queries/city/load/"+id, getOrderUncityOK, getOrderUncityERROR);
} //getOrderUncity
function getOrderUncityOK(response) {
    let city = JSON.parse(response);
    document.getElementById("uncityname").value = city.name;
}
function getOrderUncityERROR(number) {
    alert("getOrderUncityERROR. ERROR "+ number);
}
