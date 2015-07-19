#!/bin/bash

PORT=3201 node server &
PORT=3202 node server &
PORT=3203 node server &
node bounce
