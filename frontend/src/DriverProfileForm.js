import React, { useState, useEffect } from "react";
import "./DriverProfileForm.css"
function DriverProfileForm({ setIsEditingProfile, user }) {
  const [profile, setProfile] = useState({
    age: "",
    experience: "",
    driver_licence_type: "",
    additional_information: "",
    vin: "",
    brand: "",
    year_of_manufacture: "",
    car_type: "sedan",
    fuel_type: "",
    mileage: "",
    number: "",
    is_serviceable: false,
  });

  const [email, setEmail] = useState("");

  useEffect(() => {

    const userEmail = user.email;
    setEmail(userEmail);

    fetch(`http://localhost:8081/backend/api/driver?email=${userEmail}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      mode: "cors",
      credentials: "include",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Profile not found");
        }
        return response.json();
      })
      .then((data) => {
        setProfile({
          age: data.age || "",
          experience: data.experience || "",
          driver_licence_type: data.driver_licence_type || "",
          additional_information: data.additional_information || "",
          vin: data.vin || "",
          brand: data.brand || "",
          year_of_manufacture: data.year_of_manufacture || "",
          car_type: data.car_type || "sedan",
          fuel_type: data.fuel_type || "",
          mileage: data.mileage || "",
          number: data.number || "",
          is_serviceable: data.is_serviceable || false,
        });
      })
      .catch((error) => console.error("Error fetching profile:", error));
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setProfile({
      ...profile,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSaveChanges = (event) => {
    event.preventDefault();

    const profileData = {
      ...profile,
      email,
      age: parseInt(profile.age, 10),
      experience: parseInt(profile.experience, 10),
      year_of_manufacture: parseInt(profile.year_of_manufacture, 10),
      mileage: parseInt(profile.mileage, 10),
    };

    fetch("http://localhost:8081/backend/api/driver", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(profileData),
      mode: "cors",
      credentials: "include",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error saving profile");
        }
        return response.text();
      })
      .then((data) => {
        console.log("Profile saved:", data);
      })
      .catch((error) => console.error("Error sending data:", error));

    setIsEditingProfile(false);
  };

  return (
    <div className="Driver-profile-form">
      <form onSubmit={handleSaveChanges}>
        <label>Age:<br/>
          <input type="number" name="age" value={profile.age} onChange={handleChange} required />
        </label>
        <label>Experience (years):<br/>
          <input type="number" name="experience" value={profile.experience} onChange={handleChange} required />
        </label>
        <label>Driver Licence Type:<br/>
          <input type="text" name="driver_licence_type" value={profile.driver_licence_type} onChange={handleChange} required />
        </label>
      
        <label>VIN:<br/>
          <input type="text" name="vin" value={profile.vin} onChange={handleChange} required />
        </label>
        <label>Brand:<br/>
          <input type="text" name="brand" value={profile.brand} onChange={handleChange} required />
        </label>
        <label>Year of Manufacture:<br/>
          <input type="number" name="year_of_manufacture" value={profile.year_of_manufacture} onChange={handleChange} required />
        </label>
        <label htmlFor="car_type">Car Type:<br/>
          <select id="car_type" name="car_type" value={profile.car_type} onChange={handleChange} required>
            <option value="sedan">Sedan</option>
            <option value="suv">SUV</option>
            <option value="truck">Truck</option>
            <option value="van">Van</option>
          </select>
        </label>
        <label>Fuel Type:<br/>
          <input type="text" name="fuel_type" value={profile.fuel_type} onChange={handleChange} required />
        </label>
        <label>Mileage:<br/>
          <input type="number" name="mileage" value={profile.mileage} onChange={handleChange} required />
        </label>
        <label>Car Number:<br/>
          <input type="text" name="number" value={profile.number} onChange={handleChange} required />
        </label>
        <label>
          Serviceable:
          <input type="checkbox" name="is_serviceable" checked={profile.is_serviceable} onChange={handleChange} />
        </label>
        <label>Additional Info:<br/>
          <textarea name="additional_information" value={profile.additional_information} onChange={handleChange} />
        </label>
        <button type="submit">Save Changes</button>
      </form>
    </div>
  );
}

export default DriverProfileForm;
