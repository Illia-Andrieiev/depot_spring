import "./QueryMenu.css"
import EditQuery from "./EditQuery";
import React, { useEffect, useState } from "react";

function QueryMenu({setIsQueryMenu, user}){
    const [isAdd, setIsAdd] = useState(false);
    const [deleteId, setDeleteId] = useState(-1);
    const [isDeleteIdValid, setDeleteIdValid] = useState(false);
    const [isEdit, setIsEdit] = useState(false);
    const [isDelete, setIsDelete] = useState(false);
    const [addForm, setAddForm] = useState({
      start_datetime: "",
      start_location: "",
      destination: "",
      driver_experience: 0,
      car_type: "sedan",
      max_car_mileage: 0,
    });
    useEffect(() => {
        const savedisAddState = localStorage.getItem("isAdd");
        const savedisEditState = localStorage.getItem("isEdit");
        const savedisDeleteState = localStorage.getItem("isDelete");
        if (savedisAddState === "true") {
            setIsAdd(true);
        }
        if (savedisEditState === "true") {
            setIsEdit(true);
        }
        if (savedisDeleteState === "true") {
            setIsDelete(true);
        }
    }, []);

    const handleAdd = () => {
        setIsAdd(true);
        localStorage.setItem("isAdd", "true");
      } 
    const handleChangeAddForm = (e) => {
      setAddForm({ ...addForm, [e.target.name]: e.target.value });
      };  
    const handleSubmitAddForm = (event) => {
      event.preventDefault();
      const urlParams = new URLSearchParams(window.location.search);
      const email = user.email;
      const queryData = {
        ...addForm,
        email,
        driver_experience: String(addForm.driver_experience),    
        max_car_mileage: String(addForm.max_car_mileage),
        type: "add", 
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
        setIsAdd(false);
        localStorage.setItem("isAdd", "false");
     }  

    const handleCancelAddForm = () => {
        setIsAdd(false);
        localStorage.setItem("isAdd", "false");
      }  
      const handleSubmitDelete = async () => {
        const urlParams = new URLSearchParams(window.location.search);
        const email =user.email;
        const type = "delete";
        try {
            const response = await fetch("http://localhost:8081/backend/api/query", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                mode: "cors",
                credentials: "include",
                body: JSON.stringify({ type, email, deleteId }),
            });
            const result = await response.json();

            if (result.status === "success") {
                setDeleteIdValid(true);
                console.log(result.message); 
            } else {
                setDeleteIdValid(false);
                console.error(result.message); 
            }
            
        } catch (error) {
            console.error("Error checking UUID:", error);
            setDeleteIdValid(false);
        }
      }  
    const handleCancelDelete = () => {
        setIsDelete(false);
        localStorage.setItem("isDelete", "false");
      }  
    const handleEdit = () => {
        setIsEdit(true);
        localStorage.setItem("isEdit", "true");
      }   
    const handleDelete = () => {
        setIsDelete(true);
        localStorage.setItem("isDelete", "true");
      } 
    const handleExit = () => {
        setIsQueryMenu(false);
        localStorage.setItem("isQueryMenu", "false");
      }
    if(isEdit){
        return(<EditQuery setIsEdit={setIsEdit} user={user}/>);
    }
    if(isDelete){
        return( 
        <div>
            <p>Enter your query id:</p>
            <input
                type="number"
                value={deleteId}
                onChange={(e) => setDeleteId(e.target.value)}
                placeholder="Enter your query id"
            />
            <button onClick={handleSubmitDelete}>Delete</button>
            <button  onClick={handleCancelDelete}>
              Cancel
            </button>
        </div>
        );
    }
    if(isAdd){
        return(
            <div className="Profile-form">
                <form action="/submit" method="POST">
                    <label for="start_datetime">Start date and time:</label>
                    <input value={addForm.start_datetime} onChange={handleChangeAddForm} type="datetime-local" id="start_datetime" name="start_datetime" required/><br/><br/>

                    <label for="start_location">Start address:</label>
                    <input value={addForm.start_location} onChange={handleChangeAddForm} type="text" id="start_location" name="start_location" required/><br/><br/>

                    <label for="destination">Destination:</label>
                    <input value={addForm.destination} onChange={handleChangeAddForm} type="text" id="destination" name="destination" required/><br/><br/>

                    <label for="driver_experience">Driver expirience(years):</label>
                    <input value={addForm.driver_experience} onChange={handleChangeAddForm} type="number" id="driver_experience" name="driver_experience" min="0" required/><br/><br/>

                    <label for="car_type">Catr type:</label>
                    <select id="car_type" name="car_type" required value={addForm.car_type} onChange={handleChangeAddForm}>
                        <option value="sedan">sedan</option>
                        <option value="suv">SUV</option>
                        <option value="truck">truck</option>
                        <option value="van">van</option>
                    </select><br/><br/>

                    <label for="max_car_mileage">max mileage:</label>
                    <input value={addForm.max_car_mileage} onChange={handleChangeAddForm} type="number" id="max_car_mileage" name="max_car_mileage" min="0" required/><br/><br/>

                    <button onClick={handleSubmitAddForm}>submit</button>
               
                </form>
                <button className="buttons" onClick={handleCancelAddForm}>cancel</button>
             </div>
        );
    }
return (
      <div className="Left-buttons-div">
        <button className="buttons" onClick={handleAdd}>
          Add query
        </button>
        <button className="buttons" onClick={handleEdit}>
          Edit query
        </button>
        <button className="buttons" onClick={handleDelete}>
          Delete Query
        </button>
        <button className="buttons" onClick={handleExit}>
          Exit
        </button>
      </div>
    );
}
export default QueryMenu;