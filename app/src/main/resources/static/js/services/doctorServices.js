import { API_BASE_URL } from "../config/config.js";

const DOCTOR_API = API_BASE_URL + '/doctor'

export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' }
    });

    if (!response.ok) {
      throw new Error(`Couldn't fetch doctors: ${response.status}`);
    }
    const data = await response.json();

    return data.doctors || [];
  } catch (error) {
    console.error('Error fetching doctors:', error);
    return []; // fallback so frontend doesn’t break
  }
}



export async function deleteDoctor(id, token) {
  try {

    const fullEndpoint = `${DOCTOR_API}/${id}/${token}`;

    const response = await fetch(fullEndpoint, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' }
    });

    const data = await response.json();

    if (response.ok) {
      return {
        success: true,
        message: data.message || 'Doctor deleted successfully'
      };
    } else {
      return {
        success: false,
        message: data.message || 'Failed to delete doctor'
      };
    }
  } catch (error) {
    console.error('Error deleting doctor:', error);
    return {
      success: false,
      message: 'An unexpected error occurred while deleting doctor'
    };
  }
}

export async function saveDoctor(doctor, token) {
  try {
    const fullEndpoint = `${DOCTOR_API}/${token}`;

    const response = await fetch(fullEndpoint, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(doctor)
    });

    const data = await response.json();

    if (response.ok) {
      return {
          success: true,
        message: data.message || 'Doctor saved successfully'
      };
    } else {
      return {
        success: false,
        message: data.message || 'Failed to save doctor'
      };
    }
  } catch (error) {
    console.error('Error saving doctor:', error);
    return {
      success: false,
      message: 'An unexpected error occurred while saving doctor'
    };
  }
}


export async function filterDoctors(name = '', time = '', specialty = '') {
  try {
    const encodedName = encodeURIComponent(name || '');
    const encodedTime = encodeURIComponent(time || '');
    const encodedSpecialty = encodeURIComponent(specialty || '');

    // build URL with route parameters
    const url = `${DOCTOR_API}/?name=${encodedName}&time=${encodedTime}&speciality=${encodedSpecialty}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' }
    });

    if (!response.ok) {
      console.error('Failed to fetch filtered doctors:', response.status);
      return { doctors: [] };
    }

    const data = await response.json();
    return data.doctors || [];
  } catch (error) {
    console.error('Error filtering doctors:', error);
    alert('Failed to fetch filtered doctors. Please try again.');
    return { doctors: [] };
  }
}

/*
  Import the base API URL from the config file
  Define a constant DOCTOR_API to hold the full endpoint for doctor-related actions


  Function: getDoctors
  Purpose: Fetch the list of all doctors from the API

   Use fetch() to send a GET request to the DOCTOR_API endpoint
   Convert the response to JSON
   Return the 'doctors' array from the response
   If there's an error (e.g., network issue), log it and return an empty array


  Function: deleteDoctor
  Purpose: Delete a specific doctor using their ID and an authentication token

   Use fetch() with the DELETE method
    - The URL includes the doctor ID and token as path parameters
   Convert the response to JSON
   Return an object with:
    - success: true if deletion was successful
    - message: message from the server
   If an error occurs, log it and return a default failure response


  Function: saveDoctor
  Purpose: Save (create) a new doctor using a POST request

   Use fetch() with the POST method
    - URL includes the token in the path
    - Set headers to specify JSON content type
    - Convert the doctor object to JSON in the request body

   Parse the JSON response and return:
    - success: whether the request succeeded
    - message: from the server

   Catch and log errors
    - Return a failure response if an error occurs


  Function: filterDoctors
  Purpose: Fetch doctors based on filtering criteria (name, time, and specialty)

   Use fetch() with the GET method
    - Include the name, time, and specialty as URL path parameters
   Check if the response is OK
    - If yes, parse and return the doctor data
    - If no, log the error and return an object with an empty 'doctors' array

   Catch any other errors, alert the user, and return a default empty result
*/
