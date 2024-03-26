/**
 * ログイン処理
 */
document.getElementById('loginForm').addEventListener('submit', (e) => {
    e.preventDefault();
    const username = document.getElementById('usernameText').value;
    const password = document.getElementById('passwordText').value;
    fetch('http://localhost:8080', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    }).then(data=> {
        loadTodos();
    })
});

/**
 * TODOの取得
 */
const loadTodos = () => {
    fetch('http://localhost:8080/api/todos')
        .then(data => {
            const todoList = document.getElementById('todoList');
            todoList.innerHTML = '';
            for (const todo of data) {
                const li = document.createElement('li');
                li.innerHTML = todo.description;
                todoList.appendChild(li);
            }
        })
};

/**
 * TODOの追加
 */
document.getElementById('todoForm').addEventListener('submit', e => {
    e.preventDefault();
    const description = document.getElementById('descriptionText').value;
    fetch('http://localhost:8080/api/todos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            // TODO CORSヘッダー追加
        },
        body: JSON.stringify({
            description: description
        })
    }).then(data => {
        loadTodos();
    })
});
