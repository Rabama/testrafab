
alert("search()");

let csrf = document.getElementById("csrf");
alert(csrf.name + " / " + csrf.value);

//let token  = $("meta[name='_csrf']").attr("content");
//let header = $("meta[name='_csrf_header']").attr("content");
// let elementToken = document.querySelector('meta[property="_csrf"]');
// let token = elementToken && elementToken.getAttribute("content");
// let elementHeader = document.querySelector('meta[property="_csrf_header"]');
// let header = elementHeader && elementHeader.getAttribute("content");

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

//let data = JSON.stringify({"email": "hey@mail.com", "password": "101010"});
//xhr.setRequestHeader(header, token);
xhr.setRequestHeader(csrf.name, csrf.value);
//xhr.send(data);
xhr.send();



