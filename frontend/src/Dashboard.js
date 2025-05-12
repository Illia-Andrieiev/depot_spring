import "./Dashboard.css";
import React, { useState, useEffect } from "react";
import ProfileForm from "./ProfileForm";
import QueryTable from "./QueryTable";
import Dispatcher from "./Dispatcher";
import QueryMenu from "./QueryMenu";
import DriverDashboard from "./DriverDashboard";
function Dashboard({ setUser, user }) {
  const [isEditingProfile, setIsEditingProfile] = useState(false); 
  const [isDispatcherMenu, setIsDispatcherMenu] = useState(false);
  const [isDriverMenu, setIsDriverMenu] = useState(false);
  const [isQueryMenu, setIsQueryMenu] = useState(false);

  useEffect(() => {
    console.error("trying write user");
    console.log("User email:", user.email);
    console.log("Access token:", user.accessToken);
    console.log("ID token:", user.idToken);
    const savedDispatcherState = localStorage.getItem("isDispatcherMenu");
    const savedDriverState = localStorage.getItem("isDriverMenu");
    const savedQueryState = localStorage.getItem("isQueryMenu");
    if (savedDispatcherState === "true") {
      setIsDispatcherMenu(true);    
    }
    if (savedDriverState === "true") {
      setIsDriverMenu(true);
    }
    if (savedQueryState === "true") {
      setIsQueryMenu(true);
    }
  }, []);
  
  
  const handleLogout = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("isDispatcherMenu");
    setUser(null);
    fetch("http://localhost:8081/backend/logout", {
      method: "POST",
      credentials: "include",
    });
    
  };

  const handleChangeProfile = () => {
    console.log(user);
    setIsEditingProfile(!isEditingProfile);
  };
  const handleDispatcherMenu = () => {
    setIsDispatcherMenu(true);
    localStorage.setItem("isDispatcherMenu", "true");
  } 
  const handleDriverMenu = () => {
    setIsDriverMenu(true);
    localStorage.setItem("isDriverMenu", "true");
  } 
  const handleQueryMenu = () => {
    setIsQueryMenu(true);
    localStorage.setItem("isQueryMenu", "true");
  } 



  if(isQueryMenu){
    return <QueryMenu setIsQueryMenu ={setIsQueryMenu} user={user}/>
  }
  if(isDriverMenu){
    return <DriverDashboard user ={user} setIsDriverMenu={setIsDriverMenu} /> 
  }
  return (
    isDispatcherMenu ? (
      <Dispatcher setIsDispatcherMenu={setIsDispatcherMenu} user={user}/>
    ) : (
      <div>
        <div className="Main-div">
          <div className="Left-buttons-div">
            <button className="buttons" onClick={handleDispatcherMenu}>
              I'm dispatcher
            </button>
            <button className="buttons" onClick={handleDriverMenu}>
              I'm driver
            </button>
            <button className="buttons" onClick={handleQueryMenu}>
              Order dispatch
            </button>
          </div>
          <div className="Center-table-div">
            <QueryTable  user={user}/>
          </div>
          <div className="Right-buttons-div">
            <button className="buttons" onClick={handleLogout}>
              Logout
            </button>
            <button className="buttons" onClick={handleChangeProfile}>
              Change Profile
            </button>
          </div>
        </div>
        {isEditingProfile && <ProfileForm setIsEditingProfile={setIsEditingProfile}  user={user}/>}
      </div>
    )
  );
}

export default Dashboard;