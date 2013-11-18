Autocomplete Product Listing with Play
======================================

Simple listing item page with autocomplete search and inline updating use Scala Play Framework, AngularJs, Bootstrap and Mongodb.

To prepare the database, do import:

`mongoimport --db catalogdb --collection catalogs --file catalog.json`

Update application.conf of following key to poin to correct database:

`mongo.database=catalogdb`

Please note, currently mongodb server that will be used should disable its authentication, because the code has not handled the mongodb authentication yet. There are also no any unit test yet. Still need time to learn how to unit test properly use scala and javascript.

After check out: 

`cd play-autocomplete`

and execute 

`play run`

Open browser and go to [http://localhost:9000/pages/index.html](http://localhost:9000/pages/index.html)

TODO
====
* Unit Test (in TDD this supposed to do first :D)
* Complete CRUD 
* Application Authentication

(I was planning to write the whole CRUD even plus web application authentication. However, even for this simple page is little bit harder that I expected. Need some more time to get used with the whole stacks)

[r2h]: lib/github/commands/rest2html
[r2hc]: lib/github/markups.rb#L51


