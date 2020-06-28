curl -X GET -i http://localhost:8080/topjava/rest/meals

curl -X POST -H 'Content-Type: application/json' -i http://localhost:8080/topjava/rest/meals --data '{
                        "dateTime": "2015-08-30T13:00:00",
                        "description": "Обед",
                        "calories": 1000
                        }'
                        
curl -X GET -i 'http://localhost:8080/topjava/rest/meals/filter?startDate=2015-05-31&startTime=11:00'

curl -X GET -i http://localhost:8080/topjava/rest/meals/100002

curl -X PUT -H 'Content-Type: application/json' -i http://localhost:8080/topjava/rest/meals/100002 --data '{
                        "id": 100002,
                        "dateTime": "2017-06-30T13:00",
                        "description": "Обед Обновленный",
                        "calories": 1000
                        }'
                        
curl -X DELETE -i http://localhost:8080/topjava/rest/meals/100002

