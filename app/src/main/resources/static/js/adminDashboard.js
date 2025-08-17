import { openModal } from '../components/modals.js';
import { getDoctors , filterDoctors , saveDoctor } from './services/doctorServices.js';
import { createDoctorCard  } from './components/doctorCard.js';

window.onload(function(){
    document.getElementById('addDocBtn').addEventListener('click', () => {
    openModal('addDoctor');
    });

    loadDoctorCards();
    
    document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
    document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
    document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);
});

function filterDoctorsOnChange() {
    const searchBar = document.getElementById("searchBar").value.trim();
    const filterTime = document.getElementById("filterTime").value;
    const filterSpecialty = document.getElementById("filterSpecialty").value;
  
  
    const name = searchBar.length > 0 ? searchBar : null;
    const time = filterTime.length > 0 ? filterTime : null;
    const specialty = filterSpecialty.length > 0 ? filterSpecialty : null;
  
    filterDoctors(name, time, specialty)
      .then(response => {
        const doctors = response.doctors;
        const contentDiv = document.getElementById("content");
        contentDiv.innerHTML = "";
  
        if (doctors.length > 0) {
          console.log(doctors);
          doctors.forEach(doctor => {
            const card = createDoctorCard(doctor);
            contentDiv.appendChild(card);
          });
        } else {
          contentDiv.innerHTML = "<p>No doctors found with the given filters.</p>";
          console.log("Nothing");
        }
      })
      .catch(error => {
        console.error("Failed to filter doctors:", error);
        alert("❌ An error occurred while filtering doctors.");
      });
  }

  
export function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";
  
    doctors.forEach(doctor => {
      const card = createDoctorCard(doctor);
      contentDiv.appendChild(card);
    });
  
  }
  
  export async function adminAddDoctor(event) {
    event.preventDefault(); // prevent form from submitting normally
  
    // Collect form inputs
    const name = document.querySelector('#doctorName').value;
    const specialty = document.querySelector('#specialization').value;
    const email = document.querySelector('#doctorEmail').value;
    const password = document.querySelector('#doctorPassword').value;
    const phone = document.querySelector('#doctorPhone').value;
  
    // Collect availability checkboxes
    const availability = Array.from(document.querySelectorAll('input[name="availability"]:checked'))
      .map(cb => cb.value);
  
    // Retrieve admin token
    const token = localStorage.getItem('token');
    if (!token) {
      alert('You must be logged in as admin to add a doctor.');
      return;
    }
  
    const doctor = { name, specialty, email, password, phone, availability };
  
    try {
      const response = await saveDoctor(doctor, token);
  
      if (response.success) {
        alert('Doctor added successfully!');
        // Close modal and optionally refresh doctor list
        openModal('addDoctorModal', 'close'); // assuming openModal supports closing
        location.reload(); // or call a function to refresh doctor list dynamically
      } else {
        alert('Failed to add doctor: ' + response.message);
      }
    } catch (error) {
      console.error('Error adding doctor:', error);
      alert('An error occurred while adding the doctor. Please try again.');
    }
  }
  

  const saveDoctorBtn = document.querySelector('#saveDoctorBtn');
  if (saveDoctorBtn) {
    saveDoctorBtn.addEventListener('click', adminAddDoctor);
  }
  

/*
  This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form


  Attach a click listener to the "Add Doctor" button
  When clicked, it opens a modal form using openModal('addDoctor')


  When the DOM is fully loaded:
    - Call loadDoctorCards() to fetch and display all doctors


  Function: loadDoctorCards
  Purpose: Fetch all doctors and display them as cards

    Call getDoctors() from the service layer
    Clear the current content area
    For each doctor returned:
    - Create a doctor card using createDoctorCard()
    - Append it to the content div

    Handle any fetch errors by logging them


  Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  On any input change, call filterDoctorsOnChange()


  Function: filterDoctorsOnChange
  Purpose: Filter doctors based on name, available time, and specialty

    Read values from the search bar and filters
    Normalize empty values to null
    Call filterDoctors(name, time, specialty) from the service

    If doctors are found:
    - Render them using createDoctorCard()
    If no doctors match the filter:
    - Show a message: "No doctors found with the given filters."

    Catch and display any errors with an alert


  Function: renderDoctorCards
  Purpose: A helper function to render a list of doctors passed to it

    Clear the content area
    Loop through the doctors and append each card to the content area


  Function: adminAddDoctor
  Purpose: Collect form data and add a new doctor to the system

    Collect input values from the modal form
    - Includes name, email, phone, password, specialty, and available times

    Retrieve the authentication token from localStorage
    - If no token is found, show an alert and stop execution

    Build a doctor object with the form values

    Call saveDoctor(doctor, token) from the service

    If save is successful:
    - Show a success message
    - Close the modal and reload the page

    If saving fails, show an error message
*/
