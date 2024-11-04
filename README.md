###3.3.2

GET ALL TRIPS, I ONLY TOOK ONE WITH THE RESPONSE
GET http://localhost:9090/api/trips
HTTP/1.1 200 OK
[
{
"id": 4,
"name": "Mountain Hike",
"starttime": [
2024,
5,
1,
9,
0
],
"endtime": [
2024,
5,
1,
17,
0
],
"longitude": -105.2705,
"latitude": 40.015,
"price": 250.0,
"category": "SNOW"
},
Response file saved.
> 2024-11-04T124129.200.json

Response code: 200 (OK); Time: 143ms (143 ms); Content length: 1366 bytes (1,37 kB)

GET TRIP BY ID:
GET http://localhost:9090/api/trips/4

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 11:43:29 GMT

{
"trip": {
"id": 4,
"name": "Mountain Hike",
"starttime": [
2024,
5,
1,
9,
0
],
"endtime": [
2024,
5,
1,
17,
0
],
"longitude": -105.2705,
"latitude": 40.015,
"price": 250.0,
"category": "SNOW"
},
"guide": {
"id": 1,
"firstname": "John",
"lastname": "Doe",
"email": "john@example.com",
"phone": "1234567890",
"yearsOfExperience": 5
},
"packingItems": [
{
"name": "Snow Gloves",
"weightInGrams": 200,
"quantity": 1,
"description": "Thermal gloves for snowy conditions.",
"category": "snow",
"createdAt": 1730310298.547000000,
"updatedAt": 1730310298.547000000,
"buyingOptions": [
{
"shopName": "Winter Gear",
"shopUrl": "https://shop14.com",
"price": 30.0
}
]
},
{
"name": "Winter Hiking Boots",
"weightInGrams": 1200,
"quantity": 1,
"description": "Comfortable hiking boots for winter trails.",
"category": "snow",
"createdAt": 1730310298.547000000,
"updatedAt": 1730310298.547000000,
"buyingOptions": [
{
"shopName": "Trek Essentials",
"shopUrl": "https://shop17.com",
"price": 120.0
}
]
},
{
"name": "Snow Boots",
"weightInGrams": 1500,
"quantity": 1,
"description": "Insulated boots for snowy terrain.",
"category": "snow",
"createdAt": 1730310298.547000000,
"updatedAt": 1730310298.547000000,
"buyingOptions": [
{
"shopName": "Snow Gear",
"shopUrl": "https://shop17.com",
"price": 60.0
}
]
},
{
"name": "Thermal Socks",
"weightInGrams": 100,
"quantity": 2,
"description": "Warm socks for snow trekking.",
"category": "snow",
"createdAt": 1730310298.547000000,
"updatedAt": 1730310298.547000000,
"buyingOptions": [
{
"shopName": "Winter Warmth",
"shopUrl": "https://shop18.com",
"price": 12.0
}
]
},
{
"name": "Ski Mask",
"weightInGrams": 50,
"quantity": 1,
"description": "Face protection for skiing in snow.",
"category": "snow",
"createdAt": 1730310298.547000000,
"updatedAt": 1730310298.547000000,
"buyingOptions": [
{
"shopName": "Ski Supplies",
"shopUrl": "https://shop19.com",
"price": 15.0
}
]
},
{
"name": "Snow Shovel",
"weightInGrams": 700,
"quantity": 1,
"description": "Portable shovel for clearing snow.",
"category": "snow",
"createdAt": 1730310298.547000000,
"updatedAt": 1730310298.547000000,
"buyingOptions": [
{
"shopName": "Snow Gear",
"shopUrl": "https://shop20.com",
"price": 20.0
}
]
}
]
}
Response file saved.
> 2024-11-04T124330.200.json

Response code: 200 (OK); Time: 416ms (416 ms); Content length: 2003 bytes (2 kB)

CREATE A TRIP
POST http://localhost:9090/api/trips

HTTP/1.1 201 Created
Date: Mon, 04 Nov 2024 11:44:11 GMT
Content-Type: application/json
Content-Length: 159

{
"id": null,
"name": "Mountain Hike",
"starttime": [
2024,
5,
1,
9,
0
],
"endtime": [
2024,
5,
1,
17,
0
],
"longitude": -105.2705,
"latitude": 40.015,
"price": 250.0,
"category": "SNOW"
}
Response file saved.
> 2024-11-04T124411.201.json

Response code: 201 (Created); Time: 74ms (74 ms); Content length: 159 bytes (159 B)

DO THE DATABASE
POST http://localhost:9090/api/trips/populate

HTTP/1.1 201 Created
Date: Mon, 04 Nov 2024 11:45:04 GMT
Content-Type: text/plain
Content-Length: 40

Database populated with trips and guides

Response code: 201 (Created); Time: 369ms (369 ms); Content length: 40 bytes (40 B)

PUT http://localhost:9090/api/trips/3

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 11:45:46 GMT
Content-Type: application/json
Content-Length: 164

{
"id": 3,
"name": "Updated Mountain Hike",
"starttime": [
2024,
5,
1,
9,
0
],
"endtime": [
2024,
5,
1,
18,
0
],
"longitude": -105.2705,
"latitude": 40.015,
"price": 275.0,
"category": "LAKE"
}
Response file saved.
> 2024-11-04T124546.200.json

Response code: 200 (OK); Time: 74ms (74 ms); Content length: 164 bytes (164 B)

GET http://localhost:9090/api/trips/category/SNOW

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 11:48:27 GMT
Content-Type: application/json
Content-Length: 788

[
{
"id": 11,
"name": "Mountain Hike",
"starttime": [
2024,
5,
1,
9,
0
],
"endtime": [
2024,
5,
1,
17,
0
],
"longitude": -105.2705,
"latitude": 40.015,
"price": 250.0,
"category": "SNOW"
},
{
"id": 5,
"name": "Mountain Hike",
"starttime": [
2024,
5,
1,
9,
0
],
"endtime": [
2024,
5,
1,
17,
0
],
"longitude": -105.2705,
"latitude": 40.015,
"price": 250.0,
"category": "SNOW"
},
{
"id": 4,
"name": "Mountain Hike",
"starttime": [
2024,
5,
1,
9,
0
],
"endtime": [
2024,
5,
1,
17,
0
],
"longitude": -105.2705,
"latitude": 40.015,
"price": 250.0,
"category": "SNOW"
},
{
"id": 6,
"name": "Mountain Hike",
"starttime": [
2024,
5,
1,
9,
0
],
"endtime": [
2024,
5,
1,
17,
0
],
"longitude": -105.2705,
"latitude": 40.015,
"price": 250.0,
"category": "SNOW"
},
{
"id": 10,
"name": "Mountain Hike",
"starttime": [
2024,
5,
1,
9,
0
],
"endtime": [
2024,
5,
1,
17,
0
],
"longitude": -105.2705,
"latitude": 40.015,
"price": 250.0,
"category": "SNOW"
}
]
Response file saved.
> 2024-11-04T124827.200.json

Response code: 200 (OK); Time: 156ms (156 ms); Content length: 788 bytes (788 B)

GET http://localhost:9090/api/trips/guides/totalprice

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 11:48:47 GMT
Content-Type: application/json
Content-Length: 166

[
{
"totalPrice": 400.0,
"guideId": 4
},
{
"totalPrice": 150.0,
"guideId": 6
},
{
"totalPrice": 750.0,
"guideId": 3
},
{
"totalPrice": 775.0,
"guideId": 1
},
{
"totalPrice": 500.0,
"guideId": 5
}
]
Response file saved.
> 2024-11-04T124847.200.json

Response code: 200 (OK); Time: 40ms (40 ms); Content length: 166 bytes (166 B)
GET http://localhost:9090/api/trips/4/packing-weight

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 11:49:09 GMT
Content-Type: application/json
Content-Length: 40

{
"totalPackingWeight": 3750.0,
"tripId": 4
}
Response file saved.
> 2024-11-04T124909.200.json

Response code: 200 (OK); Time: 145ms (145 ms); Content length: 40 bytes (40 B)

3.3.5
Because a post method is reserved for creating something new. Since we're not creating but updating
we use a put.