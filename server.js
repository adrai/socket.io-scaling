/**
 * use this engine.io-client: https://github.com/adrai/engine.io-client
 */

var session = require('express-session'),
    cookieParser = require('cookie-parser'),
    app = require('express')();

app.use(cookieParser('very secret'));

app.use(session({
  secret: 'very secret',
  name: 'JSESSIONID',
  resave: true,
  saveUninitialized: true
}));

app.get('/', function (req, res) {
  req.session.lastAccess = new Date().getTime();
  res.statusCode = '204';
  res.end();
});

var server = require('http').Server(app);
var io = require('socket.io')(server);


server.listen(process.env.PORT);


io.sockets.use(function (socket, next) {
  console.log(socket.handshake.headers);
  next();
});

io.sockets.on('connection', function (socket) {
  console.log('connected');
  socket.on('message', function () { });
  socket.on('disconnect', function () { });
});

console.log('started on: ' + process.env.PORT);