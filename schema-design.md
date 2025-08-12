
## MySQL Database Design

### Table: patients
- id: INT, Primary Key, Auto Increment
- title: ENUM('Mr', 'Miss', 'Mrs', 'Mz', 'Dr', 'Other'), Not Null
- first_name: VARCHAR(35), Not Null
- last_name: VARCHAR(50), Not Null
- date_of_birth: DATE, Not Null
- gender: ENUM('Male', 'Female', 'Other'), Not Null
- phone_number: VARCHAR(15), Null
- email: VARCHAR(100), Null, Unique
- full_address: TEXT, Null
- post_code: VARCHAR(10), Not Null
- emergency_contact_name: VARCHAR(100), Null
- emergency_contact_phone: VARCHAR(15), Null
- patient_notes: TEXT, Null
- medications: TEXT, Null
- created_at: DATETIME, Not Null, Default = CURRENT_TIMESTAMP

### Table: doctors
- id: INT, Primary Key, Auto Increment
- title: ENUM('Mr', 'Miss', 'Mrs', 'Mz', 'Dr', 'Other'), Not Null
- first_name: VARCHAR(35), Not Null
- last_name: VARCHAR(50), Not Null
- date_of_birth: DATE, Not Null
- gender: ENUM('Male', 'Female', 'Other'), Not Null
- phone_number: VARCHAR(15), Null
- email: VARCHAR(100), Null, Unique
- full_address: TEXT, Null
- post_code: VARCHAR(10), Not Null
- specialisation: VARCHAR(50), Not Null
- phone_number_extension: VARCHAR(5), Null
- created_at: DATETIME, Not Null, Default = CURRENT_TIMESTAMP

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- clinic_location_id: INT, Foreign Key → client_location(id)
- reason_for_visit: TEXT, Null
- further_notes: TEXT, Null
- accessibility_preferences: TEXT, Null // if patient has certain accessibility requirements that need to be followed, e.g. booking a room downstairs
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled, 3 = Not Attended) //giving the admin staff the ability to report on non attended appointment.

### Table: admin
- id: INT, Primary Key, Auto Increment
- title: ENUM('Mr', 'Miss', 'Mrs', 'Mz', 'Dr', 'Other'), Not Null
- first_name: VARCHAR(35), Not Null
- last_name: VARCHAR(50), Not Null
- date_of_birth: DATE, Not Null
- gender: ENUM('Male', 'Female', 'Other'), Not Null
- phone_number: VARCHAR(15), Null
- email: VARCHAR(100), Null, Unique
- full_address: TEXT, Null
- post_code: VARCHAR(10), Not Null
- phone_number_extension: VARCHAR(5), Null
- role: ENUM('super administrator', 'administrator', 'guest'), Not Null //can use this to determine access for these users

### Table: clinic_locations
- id: INT, Primary Key, Auto Increment
- room_number: INT, Not Null
- room_contents: TEXT, Null //potential contents inside the room, what medical equipment was available.
- room_capacity: INT, Not Null //how many people are legally allowed to be in the room
- accessibility: TEXT, Null // Notes on any potential issues with accessibility (e.g. up the stairs with no lift)
- status: ENUM('operational','repairs','discontinued'), Not Null // If a room is not usable


## MongoDB Collection Design
### Collection: prescriptions
```json
{
  "_id": "ObjectId('98jasd98jnsg')",
  "patientId": 28009080,
  "prescribingDoctorId": 8927039
  "appointmentId": "20250812-004",
  "medication": {
    {
        "name": "Ramipril",
        "dosage": "250mg",
        "doctorNotes": "Take 1 tablet daily.",
        "repeat": "1 month",
    },
    {
        "name": "Ibruprofen",
        "dosage": "250mg",
        "doctorNotes": "Take 1 tablet every 4 hours as required. No more than 4 in 24 hours.",
        "repeat": "n/a",
    },
  },
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street",
    "postcode": "CF418PT"
    "postalId": "9898hd7h3d" 
  },
  "home_delivery_required": 1,
  "priority": 10,
  "treatment_for": "High Blood Pressure, Leg Pain",
  "follow_up_appointment_required": "Yes",
  "follow_up_appointment_timeframe": "6 Months",
}