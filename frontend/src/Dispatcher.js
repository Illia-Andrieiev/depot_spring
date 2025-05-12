import "./Dispatcher.css";
import DispatcherQueryTable from "./DispatcherQueryTable";
import { useState, useEffect } from "react";


function Dispatcher({ setIsDispatcherMenu, user }) {
    const [hasDispatcherRole, setHasDispatcherRole] = useState(null);

    const handleOut = () => {
        setIsDispatcherMenu(false);
        localStorage.setItem("isDispatcherMenu", "false");
    };
    

    useEffect(() => {
      if(user.isAllowDispatcherContent === true){
        setHasDispatcherRole(true);
      }
    }, );
    
    

    if (hasDispatcherRole === null) {
        return(
         <div> 
            <p>Checking permissions...</p>
            <button onClick={handleOut}>Exit</button>
         </div>
         );
    }

    if (hasDispatcherRole) {
        return (
            <div>
                <p>Access granted! Welcome, dispatcher.</p>
                <DispatcherQueryTable user={user}/>
                <button onClick={handleOut}>Exit</button>
            </div>
        );
    }

    return (
        <div>
            <p style={{ color: "red" }}>Access denied. Dispatcher role required.</p>
            <button onClick={handleOut}>Exit</button>
        </div>
    );
}

export default Dispatcher;
