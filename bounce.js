var bouncy = require('bouncy'),
  cookie = require('cookie');

var store = {};
var position = 0;
var ports = [ 3201, 3202, 3203 ];

bouncy(function (req, bounce) {

  var cookies = req.headers.cookie ? cookie.parse(req.headers.cookie) : null;
  console.log('PROXY cookies, found:');
  console.log(cookies);

  if (!cookies || !cookies['JSESSIONID']) {
    console.log('PROXY - redirects to: ' + ports[position]);
    bounce(ports[position]);
    position = (position + 1) % 3;
    return;
  }

  if (!store[cookies['JSESSIONID']]) {
    store[cookies['JSESSIONID']] = ports[position];
  }

  console.log('redirect to: ' + store[cookies['JSESSIONID']]);
  bounce(store[cookies['JSESSIONID']]);

}).listen(3200);

console.log('PROXY started on: 3200');
