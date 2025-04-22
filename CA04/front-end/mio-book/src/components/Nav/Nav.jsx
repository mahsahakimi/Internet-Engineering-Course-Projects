import React from 'react';
import styles from '../Nav.css';
import logo from './LOGO.png';

function Nav() {
  return (
    <nav className="navbar bg-white shadow-sm mb-5">
      <div className="container-fluid d-flex">
        <Link className="navbar-brand d-flex" to="/"> {/* Use Link for navigation */}
          <div className="logo-container ms-5">
            <img src={logo} alt="Meow Logo" className="logo-cover" />
          </div>
        </Link>
        <div className="search-container d-flex col-6">
          <div className="dropdown">
            <button
              className="btn dropdown-toggle"
              type="button"
              id="dropdownMenuButton"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              Author
            </button>
            <ul className="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <li><Link className="dropdown-item" to="#">Author</Link></li> {/* Use Link */}
              <li><Link className="dropdown-item" to="#">Title</Link></li>   {/* Use Link */}
              <li><Link className="dropdown-item" to="#">Publisher</Link></li> {/* Use Link */}
            </ul>
          </div>
          <div className="border-start mx-3"></div>
          <input
            type="text"
            className="form-control search-box"
            placeholder="Search"
          />
        </div>
        <button className="btn green-btn">Buy now</button>
      </div>
    </nav>
  );
}

export default Nav;