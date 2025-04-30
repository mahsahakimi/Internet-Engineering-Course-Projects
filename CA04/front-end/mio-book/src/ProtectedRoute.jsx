import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { UserContext } from './UserContex';

function ProtectedRoute({ children }) {
    const { user } = useContext(UserContext);
    const localUser = !user ? JSON.parse(localStorage.getItem('logged_user')) : null;
    const displayUser = user || localUser;

    if (!displayUser) {
        return <Navigate to="/signin" replace />;
    }

    return children;
}

export default ProtectedRoute;