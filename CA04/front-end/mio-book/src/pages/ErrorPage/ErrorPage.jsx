import React from 'react';
import { useLocation } from 'react-router-dom';
import { Link } from 'react-router-dom';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import '../../stylesheets/General.css';
import '../../stylesheets/ErrorGeneral.css';

function ErrorPage() {
    const location = useLocation();
    const errorMessage = location.state?.message || 'An unexpected error occurred.';

    return (
    <body>
        <Nav />
        <div className="main-content p-2 mb-2 d-flex align-items-center justify-content-center">
            <div className="error-container col-sm-10 d-flex">
                <div className="error-illustration col-sm-4 d-flex align-items-center justify-content-center">
                    <h2>Oops!</h2>
                    <p>Something Went Wrong.</p>
                </div>
                <div className="col-sm-8 p-5">
                    <span className="col-4 btn gray-btn">ERROR</span>
                    <h1>Please try again later.</h1>
                    <p></p>
                    <div className="error-message p-2 mb-5 col-12 justify-content-center">
                        {errorMessage}
                    </div>
                    <div className="d-flex col-12 justify-content-center">
                        <Link className="col-sm-6 green-btn btn" to="/">
                          Return Home
                        </Link>
                    </div>
                </div>
            </div>
        </div>
        <Footer />
    </body>
    );
};

export default ErrorPage;