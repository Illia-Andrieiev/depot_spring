import "./QueryTable.css";
import React, { useState, useEffect } from "react";

function DriverQueryTable({user}) {
  const [trips, setTrips] = useState([]);
  const [showInput, setShowInput] = useState({});
  const [mileageInput, setMileageInput] = useState({});
  const [endTimeInput, setEndTimeInput] = useState({});
  const [serviceableInput, setServiceableInput] = useState({});

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const userEmail = user.email;
    const requestBody = {
      email: userEmail,
      type: "get_assigned",
    };

    fetch("http://localhost:8081/backend/api/trip", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      mode: "cors",
      credentials: "include",
      body: JSON.stringify(requestBody),
    })
      .then((response) => {
        if (!response.ok) throw new Error("Failed to fetch trips");
        return response.json();
      })
      .then((data) => {
        setTrips(data);
      })
      .catch((error) => {
        console.error("Error fetching trips:", error);
      });
  }, []);

  const handleAssignClick = (tripId) => {
    setShowInput((prev) => ({ ...prev, [tripId]: !prev[tripId] }));
  };

  const handleSubmit = (tripId) => {
    const mileage = mileageInput[tripId];
    const endDatetime = endTimeInput[tripId];
    const serviceable = !!serviceableInput[tripId]; // convert to boolean
    const urlParams = new URLSearchParams(window.location.search);
    const email = user.email;

    if (!mileage || !endDatetime) {
      alert("Please fill in mileage and end time.");
      return;
    }

    const payload = {
      tripId,
      email,
      mileage,
      endDatetime,
      serviceable,
      type:"finish",
    };

    fetch("http://localhost:8081/backend/api/trip", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      mode: "cors",
      credentials: "include",
      body: JSON.stringify(payload),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to submit trip data");
        alert(`Trip ${tripId} completed`);
      })
      .catch((err) => {
        console.error("Submission error:", err);
      });
  };

  return (
    <table className="center-table">
      <caption>My dispatch queries</caption>
      <thead>
        <tr>
          <th>ID</th>
          <th>From</th>
          <th>To</th>
          <th>Email</th>
          <th>Start Time</th>
          <th>End Time</th>
          <th>Mileage</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        {trips.length > 0 ? (
          trips.map((trip) => (
            <React.Fragment key={trip.id}>
              <tr>
                <td>{trip.id}</td>
                <td>{trip.startLocation}</td>
                <td>{trip.destination}</td>
                <td>{trip.email}</td>
                <td>{trip.startDatetime}</td>
                <td>{trip.endDatetime || "—"}</td>
                <td>{trip.mileage || "—"}</td>
                <td>
                  <button onClick={() => handleAssignClick(trip.id)}>
                    {showInput[trip.id] ? "Hide" : "Finish"}
                  </button>
                </td>
              </tr>
              {showInput[trip.id] && (
                <tr>
                  <td colSpan="8">
                    <label>
                      Mileage:{" "}
                      <input
                        type="number"
                        value={mileageInput[trip.id] || ""}
                        onChange={(e) =>
                          setMileageInput((prev) => ({
                            ...prev,
                            [trip.id]: e.target.value,
                          }))
                        }
                      />
                    </label>
                    &nbsp;&nbsp;
                    <label>
                      End Time:{" "}
                      <input
                        type="datetime-local"
                        value={endTimeInput[trip.id] || ""}
                        onChange={(e) =>
                          setEndTimeInput((prev) => ({
                            ...prev,
                            [trip.id]: e.target.value,
                          }))
                        }
                      />
                    </label>
                    &nbsp;&nbsp;
                    <label>
                      Serviceable:{" "}
                      <input
                        type="checkbox"
                        checked={!!serviceableInput[trip.id]}
                        onChange={(e) =>
                          setServiceableInput((prev) => ({
                            ...prev,
                            [trip.id]: e.target.checked,
                          }))
                        }
                      />
                    </label>
                    &nbsp;&nbsp;
                    <button onClick={() => handleSubmit(trip.id)}>Submit</button>
                  </td>
                </tr>
              )}
            </React.Fragment>
          ))
        ) : (
          <tr>
            <td colSpan="8" style={{ textAlign: "center" }}>
              No assigned trips found.
            </td>
          </tr>
        )}
      </tbody>
    </table>
  );
}

export default DriverQueryTable;
