var io = require('socket.io/node_modules/socket.io-client');
var http = require('http');

var options = {
  hostname: 'localhost',
  port: 3200,
  path: '/',
  method: 'GET'
};

var req = http.request(options, function(res) {
  console.log('STATUS: ' + res.statusCode);
  console.log('HEADERS: ' + JSON.stringify(res.headers));
  res.setEncoding('utf8');
  res.on('data', function (chunk) {
    console.log('BODY: ' + chunk);
  });

  var cookies;
  if (res.headers && res.headers['set-cookie'] && res.headers['set-cookie'].length > 0) {
    cookies = res.headers['set-cookie'].join(';');
  }

  var socket;

  if (cookies) {
    socket = io('http://localhost:3200', {
      extraHeaders: {
        'Cookie': cookies
      }
    });
  } else {
    socket = io('http://localhost:3200');
  }

  socket.on('connect', function () {
    console.log('connected');

    socket.on('data', function (data) {
      console.log(data);
    });

    var i = 0;
    setInterval(function () {
      socket.emit('data', 'message_' + i);
      i++;
    }, 1000);
  });

  socket.on('disconnect', function () {
    console.log('disconnected');
  });

  socket.on('error', function (err) {
    console.log('error', err);
  });
});

req.on('error', function(e) {
  console.log('problem with request: ' + e.message);
});

req.end();
