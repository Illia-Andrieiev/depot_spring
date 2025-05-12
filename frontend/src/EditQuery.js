import React, { useEffect, useState } from "react";

function EdtQuery({setIsEdit, user}){
   const [isValidId, setIsValidId] = useState(false);
    const [editId, setEditId] = useState(-1);
    const [editForm, setEditForm] = useState({
        start_datetime: "",
        start_location: "",
        destination: "",
        driver_experience: 0,
        car_type: "sedan",
        max_car_mileage: 0,
      });

      const handleSubmitEditForm = (event) => {
        event.preventDefault();
        const urlParams = new URLSearchParams(window.location.search);
        const email = user.email;
        const queryData = {
          ...editForm,
          email,
          driver_experience: String(editForm.driver_experience),    
          max_car_mileage: String(editForm.max_car_mileage),
          type: "update",
          editId, 
        };
      
        fetch("http://localhost:8081/backend/api/query", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(queryData),
          mode: "cors",
          credentials: "include",
        })
          .then((response) => {
            if (!response.ok) {
              throw new Error("Error saving query");
            }
            return response.text();
          })
          .then((data) => {
            try {
              const jsonData = JSON.parse(data);
              console.log("query saved:", jsonData);
            } catch (error) {
              console.log("Query saved (non-JSON response):", data);
            }
          })
          .catch((error) => console.error("Error sending data:", error));
          setIsEdit(false);
          localStorage.setItem("isEdit", "false");
       }  
  
    const handleCancelEditForm = () => {
    setIsEdit(false);
    localStorage.setItem("isEdit", "false");
  }  
  const handleChangeEditForm = (e) => {
    setEditForm({ ...editForm, [e.target.name]: e.target.value });
    };  

  const handleSubmitEdit = () => {
      const urlParams = new URLSearchParams(window.location.search);
      const email = user.email;
      const type = "get_id";
          fetch("http://localhost:8081/backend/api/query", {
              method: "POST",
              headers: {
                  "Content-Type": "application/json",
              },
              mode: "cors",
              credentials: "include",
              body: JSON.stringify({ type, email, editId }),
          }).then((response) => {
            if (!response.ok) {
              throw new Error("Error recieve query");
            }
            return response.text();
          })
          .then((data) => {
            try {
              const jsonData = JSON.parse(data);
              console.log("Query recieved:", jsonData);
              setEditForm({
                start_datetime: jsonData.startDatetime,
                start_location: jsonData.startLocation,
                destination: jsonData.destination,
                driver_experience: jsonData.driverExperience,
                car_type: jsonData.carType,
                max_car_mileage: jsonData.maxCarMileage,
              });
              if(jsonData.status == 'error'){
                setIsValidId(false);
              }else{   
                setIsValidId(true);
            }
            }catch (error) {
              console.log("Query recieved (non-JSON response):", data);
            }
          })
          .catch((error) => console.error("Error sending data:", error));
    }  
  const handleCancelEdit = () => {
      setIsEdit(false);
      localStorage.setItem("isEdit", "false");
    }  


  if(isValidId){
  return(
    <div className="Profile-form">
        <form action="/submit" method="POST">
            <label for="start_datetime">Start date and time:</label>
            <input value={editForm.start_datetime} onChange={handleChangeEditForm} type="datetime-local" id="start_datetime" name="start_datetime" required/><br/><br/>

            <label for="start_location">Start address:</label>
            <input value={editForm.start_location} onChange={handleChangeEditForm} type="text" id="start_location" name="start_location" required/><br/><br/>

            <label for="destination">Destination:</label>
            <input value={editForm.destination} onChange={handleChangeEditForm} type="text" id="destination" name="destination" required/><br/><br/>

            <label for="driver_experience">Driver expirience(years):</label>
            <input value={editForm.driver_experience} onChange={handleChangeEditForm} type="number" id="driver_experience" name="driver_experience" min="0" required/><br/><br/>

            <label for="car_type">Catr type:</label>
            <select id="car_type" name="car_type" required value={editForm.car_type} onChange={handleChangeEditForm}>
                <option value="sedan">sedan</option>
                <option value="suv">SUV</option>
                <option value="truck">truck</option>
                <option value="van">van</option>
            </select><br/><br/>

            <label for="max_car_mileage">max mileage:</label>
            <input value={editForm.max_car_mileage} onChange={handleChangeEditForm} type="number" id="max_car_mileage" name="max_car_mileage" min="0" required/><br/><br/>

            <button onClick={handleSubmitEditForm}>submit</button>
       
        </form>
        <button className="buttons" onClick={handleCancelEditForm}>cancel</button>
     </div>
);

}
return( 
  <div>
      <p>Enter your query id:</p>
      <input
          type="number"
          value={editId}
          onChange={(e) => setEditId(e.target.value)}
          placeholder="Enter your query id"
      />
      <button onClick={handleSubmitEdit}>Edit</button>
      <button  onClick={handleCancelEdit}>
        Cancel
      </button>
      {isValidId === false && <p style={{ color: "red" }}>Query with this id do not exist or already accepted.</p>}
  </div>
  );
}
export default EdtQuery;