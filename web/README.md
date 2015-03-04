# Team 21 Backend README



## API Documentation v3
###### Luke Whiteley

Be wary when copying directly from here into something such as curl or Postman, it may change the formatting.

### Sign Up

**ENDPOINT**: https://astervo.herokuapp.com/api/registrations

**TYPE:** POST

**HEADERS:** 

* ` Content-Type: application/json`
*  `Accept: application/json `

**PAYLOAD:** 
``` javascript
{ "User" : {
  "email" : "user@example.com",
  "password" : "password",
  "password_confirmation" : "password"
}}
```

**SAMPLE RETURN STRING:** 
``` javascript
{
    "success": true,
    "info": "Registered",
    "data": {
        "user": {
            "id": 16,
            "email": "example@user.com",
            "authentication_token": "nvKoWPMwY64cu7Tzrxxm",
            "created_at": "2015-03-04T00:14:50.760Z",
            "updated_at": "2015-03-04T00:14:50.814Z"
        },
        "auth_token": "nvKoWPMwY64cu7Tzrxxm"
    }
}
```

### Log In

**ENDPOINT**: https://astervo.herokuapp.com/api/sessions

**TYPE:** POST

**HEADERS:** 

* ` Content-Type: application/json`
*  `Accept: application/json `

**PAYLOAD:** 
``` javascript
{ "User" : {
  "email" : "user@example.com",
  "password" : "password",
}}
```

**SAMPLE RETURN STRING:** 
``` javascript
{
    "success": true,
    "info": "Logged in",
    "data": {
        "auth_token": "rs9bgnHQ8rqXaW6gLGy4", // This is the token used for logging out.
        "accounts": [ // The User's Accounts.
            {
                "id": 1,
                "type": "ISA",
                "balance": 2703,
                "interest": 7.5
            },
            {
                "id": 2,
                "type": "DEBIT",
                "balance": 1597,
                "interest": 7.5
            }
        ]
    }
}
```

### Log Out

**ENDPOINT**: https://astervo.herokuapp.com/api/sessions/?auth_token= `auth_token_here`

**TYPE:** DELETE

**HEADERS:** 

* ` Content-Type: application/json`
*  `Accept: application/json `

**PAYLOAD:** 
``` python
None.
```

**SAMPLE RETURN STRING:** 
``` javascript
{
    "success": true,
    "info": "Logged out",
    "data": {}
}
```

### Transfer Money between Accounts.

**ENDPOINT**: https://astervo.herokuapp.com/api/transfers

**TYPE:** POST

**HEADERS:** 

* ` Content-Type: application/json`
*  `Accept: application/json `

**PAYLOAD:** 
``` javascript
{"transfer": {
  "to":"1", // The ID of the account you're transferring from.
  "from":"2" // The ID of the account you're transferring to.
}, "amount":"900"} // The amount you wish to transfer.
```

**SAMPLE RETURN STRING:** 
``` javascript
{
    "result": {
        "success": true,
        "message": "Transfer complete."
    },
    "accounts": [ // An array of the User's Accounts with their updated balances.
        {
            "id": 1,
            "type": "ISA",
            "balance": 2703,
            "interest": 7.5
        },
        {
            "id": 2,
            "type": "DEBIT",
            "balance": 1597,
            "interest": 7.5
        }
    ]
}
```
