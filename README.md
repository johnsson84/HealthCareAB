# HealthCareAB
This is an online doctor booking application and this is the backend 
for it.

# API calls
## AuthController /auth
### Login
POST /login
```
{
"username": "",
"password": ""
}
```
### Register a new user
POST /register
{
"username": "",
"password": "",
"mail": "",
"firstName": "",
"lastName": ""
}

### Register admin
POST /register/admin
{
"username": "",
"password": "",
"mail": "",
"firstName": "",
"lastName": ""
}

### Register a caregiver
POST /register/caregiver/{facilityId}
{
"username": "",
"password": "",
"mail": "",
"firstName": "",
"lastName": ""
}

### Log out
POST /logout

### Check authentication
POST /check

### Forgot password
POST /forgot-password
{
"email": ""
}

### Reset password
POST /reset-password
{
"token": "",
"newPassword": ""
}

## AppointmentController /appointment
### Create a new appointment
POST /new
{
"username": "",
"caregiverId": "",
"reason": "",
"availabilityId": "",
"availabilityDate": "example 2025-01-24T07:00:00.000+00:00"
}

### Get users appointments
GET /{username}

### Get completed users appointments
GET /get/scheduled/user/{username}

### Get completed caregivers appointments (doktor admin)
GET /get/scheduled/caregiver/{username}

### Change status of an appointment
POST /change-status/{status}/{appointmentId}

### Get info about appointment
GET /info/{appointmentId}

### Get info with names
GET /info/no-id/{appointmentId}

### Appointment history for patient
GET /history/1/{username}

### Appointment history for caregiver
GET /history/2/{username}

### Add documentation to an appointment
PUT /documentation/add
{
"appointmentId": "",
"documentation": ""
}

## AvailabilityController /availability
### Set availability times for all caregivers
POST /set/all

### Set availability times for one caregiver
POST /set/one ('DOCTOR', 'ADMIN')
{
"careGiverId": ""
}

### Get list of all availability ('USER','DOCTOR', 'ADMIN')
GET

### Get list of availability for caregiver username
GET /find-by-username/{username}

### Remove availability hours ('DOCTOR')
PUT /remove-availability
{
"changingDates": ["2025-01-09T07:30:00", "2025-01-09T08:00:00"],
"availabilityId": ""
}

### Add availability hours ('DOCTOR')
PUT /add-availability
{
"changingDates": ["2025-01-09T07:30:00", "2025-01-09T08:00:00"],
"availabilityId": ""
}

## ConditionController /conditions
### Get all conditions
GET /all

### Create a condition
POST /add
{
"name": "",
"description": "",
"category": ""
}

### Delete a condition
DELETE /delete/{id}

## FacilityController /facility
### Create a facility ('ADMIN')
POST /create
{
"facilityId": "",
"facilityName": "",
"phoneNumber": "",
"email": "",
"hoursOpen": "",
"FacilityAddress": {
    "street": "",
    "city": "",
    "postcode": 12323,
    "region": "",
    "country": "",
}
"coworkersId": ["id", "id2"]
}

### List coworkers of an facility ('ADMIN', 'USER', 'DOCTOR')
GET /{facilityId}/coworkers

### List all facilities ('ADMIN', 'USER', 'DOCTOR')
GET /all

### Get one facility ('ADMIN', 'USER', 'DOCTOR')
GET /one/{id}

### Update an facility ('ADMIN')
PUT /{facilityId}
{
"facilityId": "",
"facilityName": "",
"phoneNumber": "",
"email": "",
"hoursOpen": "",
"FacilityAddress": {
"street": "",
"city": "",
"postcode": 12323,
"region": "",
"country": "",
}
"coworkersId": ["id", "id2"]
}

### Delete an facility ("hasRole('ADMIN')
DELETE /delete/{id}

### Move a coworker ('ADMIN')
POST /move/coworker/{oldFacilityId}/{newFacilityId}/{userId}

## FeedbackController /feedback
### Get all feedback ('ADMIN')
GET /all

### Get all feedback for a doctor ('DOCTOR', 'ADMIN')
GET /doctor/{username}

### Get all feedbacks given from a patient ('USER')
GET /patient/{username}

### Add a feedback to a caregiver ('USER')
POST /add
{
"appointmentId": "",
"comment": "",
"rating": 5
}

### Delete a feedback ('ADMIN')
DELETE /delete/{feedbackId}

### Get average grade doctor
GET /find/average-grade/{username}

### Get average grade all
GET /find/average-feedback/all

### A list with all doctors feedback that is high rate (4-5)
GET /find/doctors-highrating

## MailController /mail
### Send a mail from client side
POST
{
"toEmail": "",
"subject": "",
"text": "",
"appointmentReason": "",
"time": "",
"date": "",
"firstName": ""
}

### Send a mail request from client side
POST /request
{
"toEmail": "",
"subject": "",
"text": "",
"appointmentReason": "",
"time": "",
"date": "",
"firstName": ""
}

## UserController /user
### Find a user by username
GET /find/{username}

### Find user picture
GET /find/userURL/{username}

### Update user info
POST /update/user-information/{userId}
{
"mail": "",
"birthDate": "",
"phoneNmr": "",
"address": {
    "street": "",
    "city": "",
    "postcode": 12323,
    "region": "",
    "country": "",
    }
}

### Find user by ID
GET /find-userId/{userId}

### Get user fullname by ID
GET /full-name/{userId}

### Find caregivers by availability
POST /find/caregivers-by-availability
{
"userIds": ["id1", "id2"]
}

### Update a users picture
PUT update-user-picture/{username}
{
"url": ""
}