
alert("search()");

let csrf = document.getElementById("csrf");
alert(csrf.name + " / " + csrf.value);

let xhr = new XMLHttpRequest();
let url = "/query/truck";
xhr.open("POST", url, true);
xhr.setRequestHeader("Content-Type", "application/json");
xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
        let json = JSON.parse(xhr.responseText);
        console.log(json.email + ", " + json.password);
    }
};

xhr.setRequestHeader(csrf.name, csrf.value);
xhr.send();



