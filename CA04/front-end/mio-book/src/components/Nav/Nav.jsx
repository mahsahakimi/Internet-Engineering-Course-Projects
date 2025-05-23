import React, { useState } from 'react';
import '../../stylesheets/General.css';
import '../../stylesheets/navbar.css';
import { useNavigate } from 'react-router-dom';
import logo from '../../../src/images/LOGO.png';
import ProfileButton from '../ProfileButton';

function Nav() {
    const [filterType, setFilterType] = useState("Author");
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const toggleDropdown = () => setDropdownOpen(!dropdownOpen);
    const [searchTerm, setSearchTerm] = useState('');
    const navigate = useNavigate();


    const handleFilterChange = (type) => {
        setFilterType(type);
    };

    const handleSearch = async () => {
        if (!searchTerm.trim()) return;
        navigate('/results', { state: { type: filterType, term : searchTerm } })
    };

    return (
        <div>
            <nav className="navbar bg-white shadow-sm">
                <div className="container-fluid d-flex col-11">
                    <a className="navbar-brand d-flex logo-container" href="/">
                        <img src={logo} alt="Meow Logo" className="logo-cover" />
                    </a>
                    <div className="search-container d-flex col-7">
                        <div className="dropdown me-2">
                            <button
                                className="btn dropdown-toggle"
                                type="button"
                                onClick={toggleDropdown}
                                >
                                {filterType}
                            </button>
                            <ul className={`dropdown-menu ${dropdownOpen ? 'show' : ''}`}>
                                <li><button className="dropdown-item" onClick={() => { handleFilterChange("Author"); setDropdownOpen(false); }}>Author</button></li>
                                <li><button className="dropdown-item" onClick={() => { handleFilterChange("Name"); setDropdownOpen(false); }}>Name</button></li>
                            </ul>
                        </div>
                        <input
                            type="text"
                            className="form-control search-box"
                            placeholder={`Search by ${filterType}`}
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            onKeyDown={(e) => {
                                if (e.key === 'Enter') {
                                    handleSearch();
                                }
                            }}
                        />

                    </div>
                    <ProfileButton />
                </div>
            </nav>
        </div>
    );
}

export default Nav;