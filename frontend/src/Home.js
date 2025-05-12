import React, { useEffect, useState } from "react";
import Dashboard from "./Dashboard";

function Home() {
  const [user, setUser] = useState(null);

useEffect(() => {
  const fetchUserInfo = async () => {
    try {
      console.log("Fetching user info from backend...");

      const response = await fetch("http://localhost:8081/backend/api/userinfo", {
        method: "GET",
        credentials: "include",
        headers: {
          "Accept": "application/json"
        }
      });

      console.log("Response status:", response.status);

      if (!response.ok) {
        throw new Error(` Failed to fetch: ${response.status}`);
      }

      const data = await response.json();
      console.log("User data received:", data);
      setUser(data);
    } catch (error) {
      console.error("Error fetching user info:", error);
      setUser(null);
    }
  };

  fetchUserInfo();
}, []);


  const handleLogin = () => {
    console.log("trying redirect: ")
    window.location.href = "http://localhost:8081/backend/oauth2/authorization/auth0";
  };
  return (
    <div style={{ textAlign: "center", marginTop: "100px" }}>
      {!user ? (
        <div>
          <h2>Welcome! Please log in.</h2>
          <button onClick={handleLogin}>Log In with Auth0</button>
        </div>
      ) : (
        <div>
          <Dashboard user={user} setUser={setUser}/>
        </div>
      )}
    </div>
  );
}

export default Home;
