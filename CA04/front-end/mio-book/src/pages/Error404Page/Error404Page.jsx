import React from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import { Link } from 'react-router-dom';
import '../../stylesheets/General.css';
import '../../stylesheets/Error404.css';

function Error404Page() {
  return (
    <body>
        <Nav />

        <section className="error-section ">
            <div className="container">
                <div className="error-box col-sm-8 text-center">
                    <p className="error-code">404</p>
                    <div className="error-divider col-sm-2 mx-auto"></div>
                    <h1 className="mb-4">SORRY</h1>

                    <div className="bookshelf d-flex m-5 justify-content-center">
                        <div className="book"></div>
                        <div className="book"></div>
                        <div className="book"></div>
                        <div className="missing-book"></div>
                        <div className="book"></div>
                        <div className="book"></div>
                        <div className="book"></div>
                    </div>

                    <p className="lead mb-4">We couldn't find that page</p>
                    <p className="mb-5">Try searching or go to MioBook's home page.</p>

                    <div className="d-flex justify-content-center">
                        <Link className="col-sm-6 green-btn btn" to="/">
                          Return Home
                        </Link>
                    </div>
                </div>
            </div>
        </section>

        <Footer />
    </body>
  );
};

export default Error404Page;