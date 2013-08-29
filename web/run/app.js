
/**
 * Module dependencies.
 */

var express = require('express'),
  routes = require('./routes'),
  user = require('./routes/user'),
  http = require('http'),
  path = require('path'),
  mongodb = require('mongodb'),
  app = express();

/*
// Set up MongoDB
if (process.env.VCAP_SERVICES) {
  var env = JSON.parse(process.env.VCAP_SERVICES);
  var mongo = env['mongodb-1.8'][0]['credentials'];
} else {
  var mongo = {
    "hostname": "localhost",
    "port": 27017,
    "username": "",
    "password": "",
    "name": "",
    "db": "db"
  };
}

var generate_mongo_url = function (obj) {
  obj.hostname = (obj.hostname || 'localhost');
  obj.port = (obj.port || 27017);
  obj.db = (obj.db || 'test');

  if (obj.username && obj.password) {
    return "mongodb://" + obj.username + ":" + obj.password + "@" + obj.hostname + ":" + obj.port + "/" + obj.db;
  } else {
    return "mongodb://" + obj.hostname + ":" + obj.port + "/" + obj.db;
  }
};

var mongourl = generate_mongo_url(mongo);

*/
var port = (3000);
var host = ('localhost');
// all environments
app.set('port', port);
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(express.cookieParser('your secret here'));
app.use(express.session());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

var MongoClient = require('mongodb').MongoClient,
    Server = require('mongodb').Server,
    db;

var mongoClient = new MongoClient(new Server('localhost', 27017));
mongoClient.open(function(err, mongoClient) {
  db = mongoClient.db('runs_test5');
  db.collection('runs', {strict:true}, function(err, collection) {
    if(err) {
      console.log("The runs collection doesn't exist, creating now");
      populateDB();
    }
  });
});

//app.get('/', routes.index);
app.get('/users', user.list);
app.get('/', function (req, res) {
  db.collection('runs', function(err, coll) {
    coll.findOne({}, {sort: {date: -1}}, function (err, item) {
      var run_data = item;
      console.log(run_data);
      res.render('index.ejs', {
        title: run_data.date,
        chart_data: run_data 
      });
    });
  });
});
app.post('/runs', function(req, res) {

  db.collection('runs', function(err, runs) {
    runs.insert(req.body, function(err) {
      if(err) throw err;
      res.send(200, req.body);
    });
  });
});

app.get('/runs', function(req, res) {
  db.collection('runs', function(err, runs) {
    runs.find({}, {sort: {date: -1}}).toArray(function(err, arr) {
      console.log(arr);
      res.write("<head></head><body>")
      for (i = 0; i < arr.length; i++) {
        res.write("<a href=/runs/" + arr[i].date +">" + arr[i].date +"</a><br />");
      }
      res.write("</body>")
      res.send();
    });
  });
});


app.get('/runs/:date', function(req, res) {
  var date = req.params.date; 
  db.collection('runs', function(err, collection) {
    collection.findOne({"date": parseInt(date)}, function(err, item) {
      console.log(item);
      //res.jsonp(item);
      var run_data = item;
      res.render('index.ejs', { title : run_data.date, chart_data: run_data} ); 
    });
  });
});

http.createServer(app).listen(app.get('port'), function () {
  console.log('Express server listening on port ' + app.get('port'));
});


var populateDB = function() {
 
    console.log("Populate db...");
    var runs = [
      { "date": 1234,
        "count": 46,
        "data": [
          "F",
          "F",
          "F",
          "H",
          "F",
          "H",
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "H",
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "H",
          "F",
          "F",
          "F",
          "F",
          "F",
          "H",
          "H",
          "H",
          "H",
          "F",
          "F",
          "F",
          "F",
          "F",
          "H",
          "F",
          "F",
          "H",
          "F"
        ]
      },
      { "date": 2222,
        "count": 10,
        "data": [
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "H"
        ]
      },
      { "date": 3333,
        "count": 10,
        "data": [
          "F",
          "F",
          "F",
          "F",
          "F",
          "F",
          "H",
          "H",
          "H",
          "H"
        ]
      },
      { "date": 4445,
        "count": 10,
        "data": [
          "F",
          "F",
          "F",
          "F",
          "H",
          "F",
          "H",
          "F",
          "H",
          "H"
        ]
      }
   ];
 
  db.collection('runs', function(err, collection) {
      collection.insert(runs, {safe:true}, function(err, result) {});
  });
 
};

