import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { Auth0Provider } from '@auth0/auth0-react';

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
 <React.StrictMode>
    <Auth0Provider
      domain="dev-j28fczur53zlmkj8.us.auth0.com"
      clientId="S17XOw90QEFbsQ3OnSMOM5vrs8dsw82P"
      authorizationParams={{
        redirect_uri: window.location.origin
      }}
    >
      <App />
    </Auth0Provider>
  </React.StrictMode>
);

// Optional performance measuring
reportWebVitals();
