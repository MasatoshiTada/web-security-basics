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
 * ログアウト処理
 */
document.getElementById('logoutForm').addEventListener('submit', async e => {
    e.preventDefault();
    const csrfToken = await getCsrfToken();
    fetch(url('/logout'), {
        method: 'POST',
        credentials: "include",
        headers: {
            'X-CSRF-TOKEN': csrfToken,
        },
    }).then(async response => {
        if (response.ok) {
            alert('ログアウトしました。');
            document.getElementById('todoList').innerHTML = '';
            document.getElementById('descriptionText').value = '';
        } else {
            const json = await response.json();
            alert(`サーバーからエラーがレスポンスされました。ステータスコード=${response.status}`);
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
                const description = DOMPurify.sanitize(todo.description);  // XSS対策
                console.log(description);
                if (todo.done === false) {
                    li.innerHTML = `<input type="text" id="descriptionText${todo.id}" value="${description}"> <button class="updateButton" onclick="updateTodo(${todo.id})">更新</button><button class="doneButton" onclick="doneTodo(${todo.id})">完了</button><button class="deleteButton" onclick="deleteTodo(${todo.id})">削除</button>`;
                } else {
                    li.innerHTML = `<s>${description}</s> <button class="deleteButton" onclick="deleteTodo(${todo.id})">削除</button>`;
                }
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
 * TODOの更新
 */
const updateTodo = async todoId => {
    const csrfToken = await getCsrfToken();
    fetch(url(`/api/todos/${todoId}`), {
        method: 'PUT',
        credentials: "include",
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken,
        },
        body: JSON.stringify({
            description: document.getElementById(`descriptionText${todoId}`).value,
        }),
    }).then(async response => {
        if (response.ok) {
            loadTodos();
        } else {
            alert(`サーバーからエラーがレスポンスされました。ステータスコード=${response.status}`);
        }
    });
};

/**
 * TODOの完了
 */
const doneTodo = async todoId => {
    const csrfToken = await getCsrfToken();
    fetch(url(`/api/todos/${todoId}`), {
        method: 'PATCH',
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
    });
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
    });
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
            'X-CSRF-TOKEN': csrfToken,
        },
        body: JSON.stringify({
            description: description,
        }),
    }).then(async response => {
        if (response.ok) {
            loadTodos();
        } else {
            alert(`サーバーからエラーがレスポンスされました。ステータスコード=${response.status}`);
        }
    });
});
