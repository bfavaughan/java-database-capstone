------------
Admin Based User Stories
------------

**Title:**
_As an admin, I want log into the portal with my own username and password, so that I can log in securely._
**Acceptance Criteria:**
1. User portal accepts a Username and a Password Field
2. Fields are validated for Null and incorrect values
3. User will be able to successfully log into the system
4. After logging in, user is confirmed logged in with a message.
**Priority:** High
**Story Points:** 5
**Notes:**
- User may need to reset their password

**Title:**
_As an admin, I want to log out of the portal, so that the system remains protected and prevents unauthorized usage._
**Acceptance Criteria:**
1. The user can click a visible “Log Out” button from any page in the portal.
2. The session is terminated immedately after logging out
3. The user is redirected to the login page after logging out
**Priority:** High
**Story Points:** 2
**Notes:**
- Ensure that tokens are invalidated after logging out and no sessions remain saved

**Title:**
_As an admin, I want to add new doctors to the portal, so that they can access the system and manage patient appointments_
**Acceptance Criteria:**
1. The admin can access an “Add Doctor” form from the dashboard.
2. The system validates required fields (e.g., name, specialty, contact details).
3. The new doctor profile is saved in the database and becomes immediately available for login (once credentials are provided)
**Priority:** High
**Story Points:** 3
**Notes:**
- Ensure assigning a doctors timetable and schedule during the creation process

**Title:**
_As an admin, I want to delete a doctor’s profile from the portal, so that I can remove outdated or inactive accounts._
**Acceptance Criteria:**
1. The admin can view a list of doctors with a “Delete” option next to each profile.
2. The system prompts for confirmation before deletion.
3. The doctor’s data is removed from active records in the database.
**Priority:** Medium
**Story Points:** 3
**Notes:**
- Potentially restrict if doctor has active appointments

**Title:**
_As an admin, I want to run a stored procedure in the MySQL CLI to get the number of appointments per month, so that I can track system usage statistics._
**Acceptance Criteria:**
1. The stored procedure accepts parameters for date range and returns monthly counts.
2. The procedure executes without errors in the MySQL CLI.
3. The output is clear and easy to interpret for reporting purposes.
**Priority:** Medium
**Story Points:** 5
**Notes:**
- In the future we could look at automating this report
- Add indexing in the data for performance

-----------
Patient User Stories
-----------

**Title:**
_As a patient user, I want to view a list of doctors without logging in, so that I can explore available options before deciding to register._
**Acceptance Criteria:**
1. The system displays a list of doctors with basic details (name, specialty, and location).
2. No authentication is required to access the doctor list.
3. Sensitive information such as contact details is hidden until registration.
**Priority:** Medium
**Story Points:** 3
**Notes:**
- Consider adding filters by location or speciality
- If doctors profiles are not active, ensure they are not shown publically.

**Title:**
_As a patient user, I want to sign up using my email and password, so that I can create an account to book appointments._
**Acceptance Criteria:**
1. The sign-up form collects email, password, and basic profile information.
2. The system validates email format and enforces password complexity.
3. Successful registration stores the account in the database and sends a confirmation email.
**Priority:** High
**Story Points:** 3
**Notes:**
- Passwords must be stored securely and encrypted

**Title:**
_As a patient user, I want to log into the portal, so that I can manage my bookings and access my account features._
**Acceptance Criteria:**
1. The login form accepts valid email and password combinations.
2. Incorrect credentials show an error message without revealing sensitive details.
3. Successful login redirects the user to their dashboard.
**Priority:** High
**Story Points:** 3
**Notes:**
- Ensure failed attempts are logged and after 5, user is locked out by IP Address

**Title:**
_As a patient user, I want to log out of the portal, so that I can secure my account and prevent unauthorized access._
**Acceptance Criteria:**
1. The user can log out from any page in the portal.
2. Logging out immediately ends the active session.
3. The user is redirected to the homepage or login screen after logout.
**Priority:** High
**Story Points:** 2
**Notes:**
- Ensure server-side sessions are removed

**Title:**
_As a patient user, I want to book an hour-long appointment with a doctor, so that I can consult with them at a convenient time._
**Acceptance Criteria:**
1. The booking form allows selecting a doctor, date, and time slot.
2. The system verifies the doctor’s availability before confirming.
3. The booking is saved in the database and visible in the user’s upcoming appointments.
4. After confirmation of booking, the user should be redirected to their portal.
**Priority:** High
**Story Points:** 4
**Notes:**
- Consider using a confirmation message on the user's profile once booking confirmed.

**Title:**
_As a patient user, I want to view my upcoming appointments, so that I can prepare accordingly._
**Acceptance Criteria:**
1. The user dashboard displays a list of upcoming confirmed appointments.
2. Each appointment shows the date, time, doctor’s name, and location.
3. Appointments are sorted in chronological order.
**Priority:** Medium
**Story Points:** 3
**Notes:**
- Display buttons for cancelling or adding new appointments in the list.
- Consider colour coding the most urgent appointments in red for visual clarity


-----------
Doctor User Stories
-----------

**Title:**
_As a doctor user, I want to log into the portal, so that I can manage my appointments._
**Acceptance Criteria:**
1. The login form accepts valid username and password credentials.
2. Incorrect credentials display an appropriate error message without exposing sensitive information.
3. Successful login redirects the doctor to their dashboard.
**Priority:** High
**Story Points:** 2
**Notes:**
- Lock the account after 5 failed login attempts

**Title:**
_As a doctor user, I want to log out of the portal, so that I can protect my data and prevent unauthorized access._
**Acceptance Criteria:**
1. The doctor can log out from any page within the portal.
2. Logging out immediately ends the active session.
3. The doctor is redirected to the login page or homepage after logout.
**Priority:** High
**Story Points:** 2
**Notes:**
- Ensure that any server-side validation is cleared and sessions are cleared

**Title:**
_As a doctor user, I want to view my appointment calendar, so that I can stay organized and manage my schedule effectively._
**Acceptance Criteria:**
1. The appointment calendar displays all confirmed appointments in a clear calendar view.
2. Appointments can be viewed by day, week, or month.
3. Clicking on an appointment displays its details.
**Priority:** Medium
**Story Points:** 4
**Notes:**
- Consider adding a colour and a key for the different appointment types and or urgency of the appointment
- Include filters to be able to quickly navigate between appointment types and dates.

**Title:**
_As a doctor user, I want to mark my unavailability, so that patients can only book available slots._
**Acceptance Criteria:**
1. The doctor can select specific dates and times to mark as unavailable.
2. Unavailable slots are hidden from the patient booking system
3. Changes to availability are reflected in real-time.
**Priority:** High
**Story Points:** 3
**Notes:**
- Allow settings for recurring unavailablity
- Provide an option to set work hours and lunch hours
- Consider an option to remove unnavailablilty that has been already set

**Title:**
_As a doctor user, I want to update my profile with specialization and contact information, so that patients have up-to-date details._
**Acceptance Criteria:**
1. The profile form allows editing specialization, phone number, and email address.
2. The system validates input fields before saving changes
3. Updated profile information is displayed to patients
**Priority:** Medium
**Story Points:** 3
**Notes:**
- May include profile photo upload functionality
- Ensure correct validation for phone numbers and email addresses using RegEx or equivelent 

**Title:**
_As a doctor user, I want to view patient details for my upcoming appointments, so that I can be prepared in advance._
**Acceptance Criteria:**
1. Each upcoming appointment includes a link or button to view patient details.
2. Patient details include name, contact information, and relevant medical history.
3. Access to patient details is restricted to the assigned doctor.
**Priority:** High
**Story Points:** 4
**Notes:**
- Consider listing any notes from previous sessions for the patient.
