const url = (path) => {
    return 'https://gmxbhyakp6.ap-northeast-1.awsapprunner.com' + path
}

/**
 * CSRFトークン取得
 */
const getCsrfToken = async () => {
    const token = await fetch(url('/api/csrf'), {
        method: 'GET',
        credentials: "include",
    }).then(async response => {
        const json = await response.json();
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
document.getElementById('loginForm').addEventListener('submit', async e => {
    e.preventDefault();
    const username = document.getElementById('usernameText').value;
    const password = document.getElementById('passwordText').value;
    const csrfToken = await getCsrfToken();
    fetch(url('/login'), {
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
        if (response.ok) {
            console.log('ログインに成功しました。TODOを取得します。');
            loadTodos();
        } else if (response.status === 401) {
            alert('ユーザー名またはパスワードが違います。');
        } else {
            const json = await response.json();
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
    fetch(url('/api/todos'), {
        credentials: "include",
    }).then(async response => {
        const json = await response.json();
        if (response.ok) {
            const todoList = document.getElementById('todoList');
            todoList.innerHTML = '';
            for (const todo of json) {
                const li = document.createElement('li');
                let description = todo.description;
                if (todo.done === true) {
                    description = '<s>' + description + '</s>';
                }
                li.innerHTML = `${description} <button class="doneButton" onclick="doneTodo(${todo.id})">完了</button><button class="deleteButton" onclick="deleteTodo(${todo.id})">削除</button>`;
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
 * TODOの完了
 */
const doneTodo = async todoId => {
    const csrfToken = await getCsrfToken();
    fetch(url(`/api/todos/${todoId}`), {
        method: 'PUT',
        credentials: "include",
        headers: {
            'X-CSRF-TOKEN': csrfToken
        },
    }).then(async response => {
        if (response.ok) {
            loadTodos();
        } else {
            alert(`サーバーからエラーがレスポンスされました。ステータスコード=${response.status}`);
        }
    })
};

/**
 * TODOの削除
 */
const deleteTodo = async todoId => {
    const csrfToken = await getCsrfToken();
    fetch(url(`/api/todos/${todoId}`), {
        method: 'DELETE',
        credentials: "include",
        headers: {
            'X-CSRF-TOKEN': csrfToken
        },
    }).then(async response => {
        if (response.ok) {
            loadTodos();
        } else {
            alert(`サーバーからエラーがレスポンスされました。ステータスコード=${response.status}`);
        }
    })
};

/**
 * TODOの追加
 */
document.getElementById('todoForm').addEventListener('submit', async e => {
    e.preventDefault();
    const description = document.getElementById('descriptionText').value;
    const csrfToken = await getCsrfToken();
    fetch(url('/api/todos'), {
        method: 'POST',
        credentials: "include",
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify({
            description: description
        })
    }).then(async response => {
        if (response.ok) {
            loadTodos();
        } else {
            alert(`サーバーからエラーがレスポンスされました。ステータスコード=${response.status}`);
        }
    })
});
