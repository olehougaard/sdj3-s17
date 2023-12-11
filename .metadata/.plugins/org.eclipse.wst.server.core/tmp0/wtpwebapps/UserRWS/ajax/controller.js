function Controller(model, view) {
    'use strict';
    var self = this;
    
    view.showUsers(model.users);
    view.observe(this);
    
    this.addUserClicked = function () {
        var data = view.userData,
            request = new XMLHttpRequest();
        request.open('POST', 'http://localhost:8080/UserRWS/users');
        request.addEventListener('load', function() {
        	self.addUser(JSON.parse(request.responseText));
        });
        request.send(JSON.stringify(data));
    };
    
    this.addUser = function (newUser) {
        newUser = model.addUser(newUser.id, newUser.name, newUser.email);
        view.addUserToTable(newUser);
    }
}