token=`curl -s --fail-with-body -XPOST -H 'Content-Type:application/json' -d@plugin-request-data.json localhost:8080/api/v1/detect/init`
echo "token: "$token
sleep 5

curl -XGET localhost:8080/report?token=$token
echo
