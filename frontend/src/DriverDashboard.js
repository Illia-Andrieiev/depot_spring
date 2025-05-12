import "./Dashboard.css";
import React, { useState, useEffect } from "react";
import DriverProfileForm from "./DriverProfileForm";
import DriverQueryTable from "./DriverQueryTable";
function DriverDashboard({setIsDriverMenu, user}) {
  const [isEditingProfile, setIsEditingProfile] = useState(false); 
  
  const handleExit = () => {
    setIsDriverMenu(false);
    localStorage.setItem("isDriverMenu", "false");
  };

  const handleChangeProfile = () => {
    setIsEditingProfile(!isEditingProfile);
  };


  return (
      <div>
        <div className="Main-div">     
        <div className="Left-buttons-div">
            <button className="buttons" onClick={handleExit}>
              exit
            </button>
            <button className="buttons" onClick={handleChangeProfile}>
              Change Profile
            </button>
          </div>
            <DriverQueryTable user={user}/>
        </div>
        {isEditingProfile && <DriverProfileForm user={user} setIsEditingProfile={setIsEditingProfile} />}
      </div>
  );
}

export default DriverDashboard;
//className="Main-div"