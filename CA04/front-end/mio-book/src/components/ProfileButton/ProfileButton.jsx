import React, { useContext, useState, useRef, useEffect } from 'react';
import { UserContext } from '../../UserContex';
import '../../stylesheets/General.css';
import '../../stylesheets/navbar.css';
import ProfileDropdown from '../ProfileDropdown';

function ProfileButton() {
    const { user } = useContext(UserContext);
    // userInfo : {
    //     "role": "customer",
    //     "username": "user22",
    //     "password": "user22",
    //     "email": "test@test.ir",
    //     "address": {
    //       "country": "iran",
    //       "city": "iran"
    //     },
    //     "balance": 0,
    //     "cart": [],
    //     "purchases": [],
    //     "ownedBooks": [],
    //     "borrowedBooks": []
    //   }
    const localUser = !user ? JSON.parse(localStorage.getItem('logged_user')) : null;
    const displayUser = user || localUser;
    const firstLetter = displayUser?.userInfo?.username?.charAt(0).toUpperCase() || '?';

    const [menuOpen, setMenuOpen] = useState(false);
    const buttonRef = useRef(null);

    // Close menu when clicking outside
    useEffect(() => {
        function handleClickOutside(event) {
            if (buttonRef.current && !buttonRef.current.contains(event.target)) {
                setMenuOpen(false);
            }
        }
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    return (
        <div className="profile-button-wrapper" ref={buttonRef} style={{ position: 'relative' }}>
            <button
                className="btn green-btn profile-btn"
                onClick={() => setMenuOpen(!menuOpen)}
            >
                {firstLetter}
            </button>

            {menuOpen && ( <ProfileDropdown name={displayUser?.userInfo?.username} />)}
        </div>
    );
}

export default ProfileButton;
