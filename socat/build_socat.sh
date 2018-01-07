#!/bin/bash
tar xzf socat-1.7.3.0.tar.gz
cd socat-1.7.3.0
./configure
make
./socat tcp-listen:1528,reuseaddr,fork tcp:localhost:1527

