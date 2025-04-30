import React from 'react';
import '../../stylesheets/General.css';
import '../../stylesheets/navbar.css';
import logo from '../../../src/images/LOGO.png';
import ProfileButton from '../ProfileButton';

function Nav() {
  return (
      <div>
          <nav className="navbar bg-white shadow-sm">
              <div className="container-fluid d-flex col-11">
                  <a className="navbar-brand d-flex logo-container" href="/">
                        <img src={logo} alt="Meow Logo" className="logo-cover" />
                  </a>
                  <div className="search-container d-flex col-7">
                      <div className="dropdown">
                          <button className="btn dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
                              Author
                          </button>
                          <ul className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                              <li><a className="dropdown-item" href="#">Author</a></li>
                              <li><a className="dropdown-item" href="#">Title</a></li>
                              <li><a className="dropdown-item" href="#">Publisher</a></li>
                          </ul>
                      </div>
                      <input type="text" className="form-control search-box" placeholder="Search" />
                  </div>
                  <ProfileButton />
              </div>
          </nav>
    </div>
  );
}

export default Nav;