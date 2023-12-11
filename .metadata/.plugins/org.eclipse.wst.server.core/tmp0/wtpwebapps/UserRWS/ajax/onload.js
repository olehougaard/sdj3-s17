function onload() {
    'use strict';
    
    var request = new XMLHttpRequest();
    request.addEventListener('load', function () {
        var users = JSON.parse(request.responseText),
        model = new Model(users),
        view = new View(),
        controller = new Controller(model, view);
    });
    request.open('GET', 'http://localhost:8080/UserRWS/users')
    request.send()
}