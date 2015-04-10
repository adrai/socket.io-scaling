/**
 * use this engine.io-client: https://github.com/adrai/engine.io-client
 */

var crypto = require('crypto');

function generateKey() {
    var sha = crypto.createHash('sha256');
    sha.update(Math.random().toString());
    return sha.digest('hex');
}

var app = require('http').createServer(function (req, res) {
  // To Write a Cookie
  res.writeHead(204, {
    'Set-Cookie': 'JSESSIONID=' + generateKey()
  });
  res.statusCode = '204';
  res.end();
});

var io = require('socket.io')(app);

io.sockets.use(function (socket, next) {
  console.log(socket.handshake.headers);
  next();
});

io.sockets.on('connection', function (socket) {
  console.log('connected');
  socket.on('data', function (input) {
	console.log(input);
  	socket.emit('data', 'server confirms ' + input);
  });
  socket.on('disconnect', function () { });
});

app.listen(process.env.PORT);

console.log('started on: ' + process.env.PORT);
