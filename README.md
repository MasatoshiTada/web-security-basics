# 自己証明書の作成

```shell
keytool -genkeypair -alias myalias -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore mykeystore.jks -validity 3650
```

