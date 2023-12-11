function Model(initialUsers) {
    'use strict';
    
    function User(id, name, email) {
        Object.defineProperties(this, {
            id: { value: id, writable: false },
            name: { value: name, writable: false },
            email: { value: email, writable: false }
        })
    }

    var users = initialUsers.map(function (u) {
        return new User(u.id, u.name, u.email);
    });

    Object.defineProperty(this, 'users', {
        get: function () { return users.slice(); }
    });
    this.addUser = function (id, name, email) {
        var newUser = new User(id, name, email);
        users.push(newUser);
        return newUser;
    };
}
