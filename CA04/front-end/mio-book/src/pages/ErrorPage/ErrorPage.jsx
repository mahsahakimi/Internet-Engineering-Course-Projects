import React from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import '../../stylesheets/General.css';
import '../../stylesheets/ErrorGeneral.css';

function ErrorPage() {
  return (
    <body>
        <Nav />
        <div className="main-content p-2 mb-2 d-flex align-items-center justify-content-center">
            <div className="error-container col-sm-10 d-flex">
                <div className="error-illustration col-sm-4 d-flex align-items-center justify-content-center">
                    <h2>Oops!</h2>
                    <p>We seem to have lost this page.</p>
                </div>
                <div className="col-sm-8 p-5">
                    <span className="col-4 btn gray-btn">Error 500</span>
                    <h1>Server Error</h1>
                    <p>Something unexpected happened on our end.</p>
                    <div className="error-message p-2 mb-5 col-12 justify-content-center">
                        Internal Server Error: The server encountered an unexpected condition.
                    </div>
                    <div className="d-flex col-12 justify-content-center">
                        <a href="/" className="btn col-6 green-btn">Return Home</a>
                    </div>
                </div>
            </div>
        </div>
        <Footer />
    </body>
  );
};

export default ErrorPage;