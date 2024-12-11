#curl -XPOST -H 'Content-Type:application/json' -d@plugin-request-data.json localhost:8080/ask
curl --fail-with-body -XPOST -H 'Content-Type:application/json' -d@plugin-request-data.json localhost:8080/detect
