import React, { useContext, useState, useRef, useEffect } from 'react';
import { UserContext } from '../../UserContex';
import '../../stylesheets/General.css';
import '../../stylesheets/Navbar.css';
import ProfileDropdown from '../ProfileDropdown';

function ProfileButton() {
    const { user } = useContext(UserContext);
    const firstLetter = user?.userInfo?.username?.charAt(0).toUpperCase() || '?';

    const [menuOpen, setMenuOpen] = useState(false);
    const buttonRef = useRef(null);

    useEffect(() => {
        if (!user) return;
        function handleClickOutside(event) {
            if (buttonRef.current && !buttonRef.current.contains(event.target)) {
                setMenuOpen(false);
            }
        }
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [user]);

    return (
        <div className="profile-button-wrapper" ref={buttonRef} style={{ position: 'relative' }}>
            <button
                className="btn green-btn profile-btn"
                onClick={() => setMenuOpen(!menuOpen)}
            >
                {firstLetter}
            </button>

            {menuOpen && ( <ProfileDropdown name={user?.userInfo?.username} />)}
        </div>
    );
}

export default ProfileButton;
