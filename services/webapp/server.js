'use strict';

const express = require('express');

var request = require('sync-request');

// Constants
const PORT = 8080;
const HOST = '0.0.0.0';

// App
const app = express();
app.get('/', (req, res) => {
    var greeter = "http://" 
    	+ process.env.GREETER_SERVICE_HOST 
    	+ ":" + process.env.GREETER_SERVICE_PORT 
    	+ "/" + process.env.GREETER_SERVICE_PATH
    	+ "?greet=" + req.query['greet'];
    var name = "http://" 
    	+ process.env.NAME_SERVICE_HOST 
    	+ ":" + process.env.NAME_SERVICE_PORT 
    	+ "/" + process.env.NAME_SERVICE_PATH
    	+ "?id=" + req.query['id'];
    console.log('greeter: ' + greeter);
    console.log('name: ' + name);

    res.send(get(greeter) + ' ' + get(name));
});

function get(url) {
	return require('sync-request')('GET', url).getBody();
}

app.listen(PORT, HOST);
console.log(`Running on http://${HOST}:${PORT}`);

