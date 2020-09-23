# Android Identification

## HMS

### Auth Service

#### Dependencies

```groovy
implementation 'com.huawei.agconnect:agconnect-core:1.4.1.300'
implementation 'com.huawei.agconnect:agconnect-auth:1.4.1.300'
```

#### Mobile Number

##### Common errors

* __Error:__ `code: 2 message: java.security.InvalidParameterException: url is null`.
* __Solution:__ Enable Auth Service (AG Console) and update `agconnect-services.json`

* __Error:__ `code: 203818052 message: verify code action is invalid`.
* __Solution:__ Add `.action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)`

##### Screenshots

![Screenshot 1](/screenshots/guide/auth1.png)
![Screenshot 2](/screenshots/guide/auth2.png)
![Screenshot 3](/screenshots/guide/auth3.png)
![Screenshot 4](/screenshots/guide/auth4.png)