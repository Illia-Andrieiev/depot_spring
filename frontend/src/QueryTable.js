import "./QueryTable.css";
import React, { useState, useEffect } from "react";


function QueryTable({user}) {
  const [queries, setQueries] = useState([]);
  
  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const userEmail =user.email;
    const requestBody = {
      email: userEmail,
      type: "get_email",
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
        if (!response.ok) {
          throw new Error("Failed to fetch queries");
        }
        return response.json();
      })
      .then((data) => {
        console.log("Received queries:", data);
        setQueries(data);
      })
      .catch((error) => {
        console.error("Error fetching queries:", error);
      });
  }, []);

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
          <th>Car Type</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        {queries.length > 0 ? (
          queries.map((query) => (
            <tr key={query.id}>
              <td>{query.id}</td>
              <td>{query.addDatetime}</td>
              <td>{query.startDatetime}</td>
              <td>{query.startLocation}</td>
              <td>{query.destination}</td>
              <td>{query.carType}</td>
              <td>{query.status}</td>
            </tr>
          ))
        ) : (
          <tr>
            <td colSpan="7" style={{ textAlign: "center" }}>
              No queries found.
            </td>
          </tr>
        )}
      </tbody>
    </table>
  );
}

export default QueryTable;
