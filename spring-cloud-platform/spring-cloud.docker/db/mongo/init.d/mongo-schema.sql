use mg
db.createUser({
   "user":"mg",
   "pwd":"mg",
   "roles":[
      {
         "role":"dbOwner",
         "db":"mg"
      }
   ]
})

> db.auth("mg", "mg")