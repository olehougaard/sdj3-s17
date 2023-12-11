function View() {
    'use strict';
    
    var tbody = document.getElementById('users');
    
    this.showUsers = function (users) {
        tbody.innerHTML = '';
        users.forEach(this.addUserToTable);
    };
    
    this.addUserToTable = function (u) {
        function td(content) {
            var td = document.createElement('td');
            td.textContent = content;
            return td;
        }
        var tr = document.createElement('tr');
        tr.appendChild(td(u.id));
        tr.appendChild(td(u.name));
        tr.appendChild(td(u.email));
        tbody.appendChild(tr);
    };
    
    Object.defineProperty(this, 'userData', {
        get: function () {
            var unField = document.getElementById('name'),
                emailField = document.getElementById('email');
            return { name: unField.value, email: emailField.value };
        }
    });
    
    this.observe = function (observer) {
        var addButton = document.getElementById('addButton');
        addButton.addEventListener('click', function () {
            observer.addUserClicked();
        });
    };
}
