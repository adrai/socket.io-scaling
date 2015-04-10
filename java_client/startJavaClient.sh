#!/bin/sh
#
# Simple equinox start script
#
cd $(dirname $0)/bin

exec java -cp .:socketIo_bundle_0.4.2.jar socket_io_java_client/SocketIoSessionHeader http://localhost:3200

