#!/usr/bin/env bash

curl -X POST -d @cnt-cell.json http://localhost:8080/cell/add-number-batch --header "Content-Type:application/json"
curl -X POST -d @profiles.json http://localhost:8080/profile/batch --header "Content-Type:application/json"
