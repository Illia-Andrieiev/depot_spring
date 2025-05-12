import "./QueryTable.css";
import React, { useState, useEffect } from "react";


function DispatcherQueryTable({user}) {
  const [queries, setQueries] = useState([]);
  const [availableDrivers, setAvailableDrivers] = useState({});
  const [selectedDrivers, setSelectedDrivers] = useState({});
  const [showSelect, setShowSelect] = useState({});

  useEffect(() => {

    const userEmail = user.email;
    const requestBody = {
      email: userEmail,
      type: "get_new",
    };

    fetch("http://localhost:8081/backend/api/query", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      mode: "cors",
      credentials: "include",
      body: JSON.stringify(requestBody),
    })
      .then((response) => {
        if (!response.ok) throw new Error("Failed to fetch queries");
        return response.json();
      })
      .then((data) => {
        setQueries(data);
      })
      .catch((error) => {
        console.error("Error fetching queries:", error);
      });
  }, []);

  const handleAssignClick = (queryId) => {
    // toggle select
    setShowSelect((prev) => ({ ...prev, [queryId]: !prev[queryId] }));

    // fetch drivers only once per query
    if (!availableDrivers[queryId]) {
        const payload = {
            queryId,
            type:"get_for_query",
          };
      fetch(`http://localhost:8081/backend/api/query/driver?id=${queryId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        mode: "cors",
        credentials: "include",
      })
        .then((res) => res.json())
        .then((data) => {
          setAvailableDrivers((prev) => ({ ...prev, [queryId]: data }));
        })
        .catch((err) => {
          console.error("Error fetching available drivers:", err);
        });
    }
  };

  const handleSubmit = (queryId) => {
    const driverId = selectedDrivers[queryId];
    if (!driverId) return;
    const urlParams = new URLSearchParams(window.location.search);
    const email = urlParams.get("user");
    const payload = {
      queryId,
      driverId,
      email,
    };

    fetch("http://localhost:8081/backend/api/query/driver", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      mode: "cors",
      credentials: "include",
      body: JSON.stringify(payload),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to assign driver");
        alert(`Driver ${driverId} assigned to query ${queryId}`);
        // Optionally update UI
      })
      .catch((err) => {
        console.error("Assignment error:", err);
      });
  };

  return (
    <table className="center-table">
      <caption>My dispatch queries</caption>
      <thead>
        <tr>
          <th>ID</th>
          <th>Added</th>
          <th>Start Time</th>
          <th>From</th>
          <th>To</th>
          <th>Experience</th>
          <th>Mileage</th>
          <th>Car Type</th>
          <th>Status</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        {queries.length > 0 ? (
          queries.map((query) => (
            <React.Fragment key={query.id}>
              <tr>
                <td>{query.id}</td>
                <td>{query.addDatetime}</td>
                <td>{query.startDatetime}</td>
                <td>{query.startLocation}</td>
                <td>{query.destination}</td>
                <td>{query.driverExperience}</td>
                <td>{query.maxCarMileage}</td>
                <td>{query.carType}</td>
                <td>{query.status}</td>
                <td>
                  <button onClick={() => handleAssignClick(query.id)}>
                    {showSelect[query.id] ? "Hide" : "Assign Driver"}
                  </button>
                </td>
              </tr>
              {showSelect[query.id] && (
                <tr>
                  <td colSpan="10">
                    <select
                      onChange={(e) =>
                        setSelectedDrivers((prev) => ({
                          ...prev,
                          [query.id]: e.target.value,
                        }))
                      }
                      value={selectedDrivers[query.id] || ""}
                    >
                      <option value="">Select driver</option>
                      {availableDrivers[query.id]?.map((driver) => (
                        <option key={driver.id} value={driver.id}>
                          {driver.id} - {driver.email}
                        </option>
                      ))}
                    </select>
                    <button onClick={() => handleSubmit(query.id)}>Submit</button>
                  </td>
                </tr>
              )}
            </React.Fragment>
          ))
        ) : (
          <tr>
            <td colSpan="10" style={{ textAlign: "center" }}>
              No new queries found.
            </td>
          </tr>
        )}
      </tbody>
    </table>
  );
}

export default DispatcherQueryTable;
