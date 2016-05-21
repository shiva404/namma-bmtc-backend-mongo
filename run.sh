#!/bin/bash
for i in {1..15}
do
   curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -H "Cache-Control: no-cache" -H "Postman-Token: fdae81d3-2e25-035d-8772-0d2f2a1d4102" -d '{"routeNumber":"500","crowdLevel":"STAND",
"latitude":12.877576,"longitude":77.61906}' "http://localhost:8389/test/v1/location"
echo "\n"
done
