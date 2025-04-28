import React from 'react';
import '../../stylesheets/General.css';
import logo from '../../../src/images/LOGO.png';

function Nav() {
  return (
      <body>
          <nav className="navbar bg-white shadow-sm">
              <div className="container-fluid d-flex">
                  <a className="navbar-brand d-flex" href="/">
                      <div className="logo-container ms-5">
                          <img src={logo} alt="Meow Logo" className="logo-cover" />
                      </div>
                  </a>
                  <div className="search-container d-flex col-6">
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
                      <div className="border-start mx-3"></div>
                      <input type="text" className="form-control search-box" placeholder="Search" />
                  </div>
                  <button className="btn green-btn ">Buy now</button>
              </div>
          </nav>
    </body>
  );
}

export default Nav;