import React from "react";
import logo from "./logo.svg";
import "./Login.css";
function Login() {
    const handleLogin = () => {
        window.location.href = "http://localhost:8081/backend/login";
      };

    return ( 
        <div className="App"> 
        <header className="App-header">
           <img src={logo} className="App-logo" alt="logo" /> 
           <p>Welcome Guest!</p>
              <button onClick={handleLogin}>Login</button> 
        </header> 
        </div> ); 
}

export default Login;
