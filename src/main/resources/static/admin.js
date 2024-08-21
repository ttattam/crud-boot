document.addEventListener('DOMContentLoaded', function() {
    fetchUsers();

    document.getElementById('submitCreateUser').addEventListener('click', createUser);
    document.getElementById('submitEditUser').addEventListener('click', updateUser);
});

function fetchUsers() {
    fetch('/admin/users')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(users => {
            displayUsers(users);
        })
        .catch(error => {
            console.error('Error:', error);
            const errorMessage = document.createElement('div');
            errorMessage.className = 'alert alert-danger';
            errorMessage.textContent = 'Ошибка при загрузке списка пользователей. Пожалуйста, попробуйте позже.';
            document.querySelector('#userTable').before(errorMessage);
        });
}

function displayUsers(users) {
    const tableBody = document.getElementById('userTableBody');
    tableBody.innerHTML = '';
    users.forEach(user => {
        const row = `
            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.name || ''}</td>
                <td>${user.lastname || ''}</td>
                <td>${user.age || ''}</td>
                <td>${user.email}</td>
                <td>${user.roles.map(role => role.name).join(', ')}</td>
                <td>
                    <button onclick="editUserModal(${user.id})" class="btn btn-primary btn-sm">Редактировать</button>
                    <button onclick="deleteUser(${user.id})" class="btn btn-danger btn-sm">Удалить</button>
                </td>
            </tr>
        `;
        tableBody.innerHTML += row;
    });
}

async function createUser() {
    const form = document.getElementById('createUserForm');
    const formData = new FormData(form);

    try {
        const response = await fetch('/admin', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        $('#createUserModal').modal('hide');
        form.reset();
        fetchUsers();
    } catch (error) {
        console.error('Error creating user:', error);
        alert('Ошибка при создании пользователя');
    }
}

async function editUserModal(userId) {
    try {
        const response = await fetch(`/admin/edit/${userId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const user = await response.json();
        fillEditForm(user);
        $('#editUserModal').modal('show');
    } catch (error) {
        console.error('Error loading user for edit:', error);
        alert('Ошибка при загрузке данных пользователя');
    }
}

function fillEditForm(user) {
    document.getElementById('editUserId').value = user.id;
    document.getElementById('editUsername').value = user.username;
    document.getElementById('editName').value = user.name || '';
    document.getElementById('editLastname').value = user.lastname || '';
    document.getElementById('editAge').value = user.age;
    document.getElementById('editEmail').value = user.email;
    document.getElementById('editPassword').value = '';
    document.getElementById('editRoleUser').checked = user.roles.some(role => role.name === 'USER');
    document.getElementById('editRoleAdmin').checked = user.roles.some(role => role.name === 'ADMIN');
}

async function updateUser() {
    const form = document.getElementById('editUserForm');
    const formData = new FormData(form);

    try {
        const response = await fetch(`/admin/edit`, {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        $('#editUserModal').modal('hide');
        fetchUsers();
    } catch (error) {
        console.error('Error updating user:', error);
        alert('Ошибка при обновлении пользователя');
    }
}

async function deleteUser(userId) {
    if (confirm('Вы уверены, что хотите удалить этого пользователя?')) {
        try {
            const response = await fetch(`/admin/delete/${userId}`, {
                method: 'POST'
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            fetchUsers();
        } catch (error) {
            console.error('Error deleting user:', error);
            alert('Ошибка при удалении пользователя');
        }
    }
}