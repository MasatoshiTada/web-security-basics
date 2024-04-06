# ログイン
```shell
# CSRFトークン取得
curl -i -k https://localhost:8080/api/csrf

# ログイン
curl -i -k -X POST -H "Content-Type: application/json" -H "Cookie: JSESSIONID=D7383F4A01F0FBE42C8EF29EB178989B;" \
-H "X-CSRF-TOKEN: qpMdcQ-BdmMplim_fu9oszMOdAcX5iZjAus4J3caQbMu3M7LmKd8EDngR1QEpRGOTMJciwI3WT9z0UBONo8OQ05_d4se7quv" \
-d '{"username":"user","password":"user"}'  https://localhost:8080/login
```

# TODO取得
```shell
curl -i -k -H "Cookie: JSESSIONID=66BD0849B2D58738DAB938D807359478;" https://localhost:8080/api/todos
```