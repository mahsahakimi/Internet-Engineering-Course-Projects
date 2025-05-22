import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { UserContext } from '../../UserContex';

function ProfileDropdown({ name }) {
    const { setUser } = useContext(UserContext);
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            const response = await fetch('/api/logout', {
                method: 'POST',
                // credentials: 'include', // Important if your backend uses cookies/session
            });

            if (response.ok) {
                setUser(null);
                localStorage.removeItem('activeUser');
                navigate('/signin');
            } else {
                console.error('Logout failed.');
                navigate('/error', { state: { message: 'Logout failed.' } });
            }
        } catch (error) {
            console.error('Error while logging out:', error);
            navigate('/error', { state: { message: 'Error occurred during logout.' } });
        }
    };

    return (
        <div className="profile-menu">
            <div className="profile-menu-header">{name || 'Sample Name'}</div>
            <hr />
            <Link to="/user" className="profile-menu-item">Profile</Link>
            <Link to="/user" className="profile-menu-item">My Books</Link>
            <Link to="/cart" className="profile-menu-item">Buy Cart</Link>
            <Link to="/purchase-history" className="profile-menu-item">Purchase History</Link>
            <hr />
            <button onClick={handleLogout} className="profile-menu-item logout-btn">
                Logout
            </button>
        </div>
    );
}

export default ProfileDropdown;