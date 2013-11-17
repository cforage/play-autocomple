Simple listing item page with autocomplete search and inline updating use Scala Play Framework, AngularJs, Bootstrap and Mongodb.

To prepare the database, do import:
mongoimport --db catalogdb --collection catalogs --file catalog.json

Update application.conf of following key to poin to correct database:
mongo.database=catalogdb

Please note, currently mongodb server that will be used should disable its authentication, because the code has not handled the mongodb authentication yet. There are also no any unit test yet. Still need time to learn how to unit test properly use scala and javascript.

After check out, cd to the directory and execute play run.

Open browser and go to http://localhost:9000/pages/bootstrap/index.html



