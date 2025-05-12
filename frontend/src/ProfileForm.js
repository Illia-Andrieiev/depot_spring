import "./ProfileForm.css";
import React, { useEffect, useState } from "react";

function ProfileForm({ setIsEditingProfile, user }) {
  const [profile, setProfile] = useState({
    firstName: "",
    lastName: "",
    phone: "",
    address: "",
    day: 1,
    month: 1,
    year: 2025,
  });
  const [email, setEmail] = useState("");

  const daysInMonth = (month, year) => new Date(year, month, 0).getDate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    console.log(user);
    const userEmail = user.email;
    setEmail(userEmail);
    
    fetch(`http://localhost:8081/backend/api/profile?email=${userEmail}`, {
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
        console.log("profile data:");
        console.log(data);
        setProfile({
          firstName: data.firstName || "",
          lastName: data.lastName || "",
          phone: data.phone || "",
          address: data.address || "",
          day: data.birthDay || 1,
          month: data.birthMonth || 1,
          year: data.birthYear || 2025,
        });
        console.log(profile);
      })
      .catch((error) => console.error("Error fetching profile:", error));
  }, []);

  const handleChange = (e) => {
    setProfile({ ...profile, [e.target.name]: e.target.value });
  };

  const handleSaveChanges = (event) => {
    event.preventDefault();
  
    const profileData = {
      ...profile,
      email,
      day: String(profile.day),    
      month: String(profile.month), 
      year: String(profile.year),  
    };
  
    fetch("http://localhost:8081/backend/api/profile", {
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
        try {
          const jsonData = JSON.parse(data);
          console.log("Profile saved:", jsonData);
        } catch (error) {
          console.log("Profile saved (non-JSON response):", data);
        }
      })
      .catch((error) => console.error("Error sending data:", error));
  
    setIsEditingProfile(false);
  };
  

  return (
    <div className="Profile-form">
      <form>
        <label>
          First Name:
          <input type="text" name="firstName" value={profile.firstName} onChange={handleChange} />
        </label>
        <label>
          Last Name:
          <input type="text" name="lastName" value={profile.lastName} onChange={handleChange} />
        </label>
        <label>
          Birthday:
          <select name="day" value={profile.day} onChange={handleChange}>
            {[...Array(daysInMonth(Number(profile.month), Number(profile.year))).keys()].map((day) => (
              <option value={day + 1} key={day} selected={profile.day === day + 1}>
                {day + 1}
              </option>
            ))}
          </select>

          <select name="month" value={profile.month} onChange={handleChange}>
            {[
              "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
            ].map((monthName, index) => (
              <option value={index + 1} key={monthName} selected={profile.month === index + 1}>
                {monthName}
              </option>
            ))}
          </select>

          <select name="year" value={profile.year} onChange={handleChange}>
            {[...Array(120).keys()].map((yearOffset) => (
              <option value={2025 - yearOffset} key={yearOffset} selected={profile.year === 2025 - yearOffset}>
                {2025 - yearOffset}
              </option>
            ))}
          </select>

        </label>
        <label>
          Phone Number:
          <input type="tel" name="phone" value={profile.phone} onChange={handleChange} />
        </label>
        <label>
          Address:
          <input type="text" name="address" value={profile.address} onChange={handleChange} />
        </label>
        <button onClick={handleSaveChanges}>Save Changes</button>
      </form>
    </div>
  );
}
export default ProfileForm;