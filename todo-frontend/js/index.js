/**
 * CSRFトークン取得
 */
const getCsrfToken = async () => {
    const token = await fetch('https://localhost:8080/api/csrf', {
        method: 'GET',
        credentials: "include",
    }).then(async response => {
        const json = await response.json();
        console.log(json);
        if (response.ok) {
            return json.token;
        } else {
            alert(`サーバーからエラーがレスポンスされました。ステータスコード=${response.status}、エラーメッセージ=${json.detail}`);
        }
    }).catch(error => {
        alert(error.message);
    });
    return token;
};

/**
 * ログイン処理
 */
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('usernameText').value;
    const password = document.getElementById('passwordText').value;
    const csrfToken = await getCsrfToken();
    fetch('https://localhost:8080', {
        method: 'POST',
        credentials: "include",
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    }).then(async response => {
        const json = await response.json();
        if (response.ok) {
            loadTodos();
        } else if (response.status === 401) {
            alert('ユーザー名またはパスワードが違います。');
        } else {
            alert(`サーバーからエラーがレスポンスされました。ステータスコード=${response.status}、エラーメッセージ=${json.detail}`);
        }
    }).catch(error => {
        alert(error.message);
    });
});

/**
 * TODOの取得
 */
const loadTodos = () => {
    fetch('https://localhost:8080/api/todos', {
        credentials: "include",
    }).then(async response => {
        const json = await response.json();
        if (response.ok) {
            const todoList = document.getElementById('todoList');
            todoList.innerHTML = '';
            for (const todo of json) {
                const li = document.createElement('li');
                li.innerHTML = todo.description;
                todoList.appendChild(li);
            }
        } else {
            alert(`サーバーからエラーがレスポンスされました。ステータスコード=${response.status}、エラーメッセージ=${json.detail}`);
        }
    }).catch(error => {
        alert(error.message);
    });
};

/**
 * TODOの追加
 */
document.getElementById('todoForm').addEventListener('submit', e => {
    e.preventDefault();
    const description = document.getElementById('descriptionText').value;
    fetch('https://localhost:8080/api/todos', {
        method: 'POST',
        credentials: "include",
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': getCsrfToken()
            // TODO CORSヘッダー追加
        },
        body: JSON.stringify({
            description: description
        })
    }).then(response => {
        if (response.ok) {
            loadTodos();
        } else {
            alert(`サーバーからエラーがレスポンスされました。ステータスコード=${response.status}、エラーメッセージ=${json.detail}`);
        }
    })
});
