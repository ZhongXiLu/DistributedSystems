#!/bin/bash
./asadmin start-domain
./asadmin change-admin-password
# setup admin password
./asadmin enable-secure-admin
# use admin and password just configured
./asadmin stop-domain
./asadmin start-domain

