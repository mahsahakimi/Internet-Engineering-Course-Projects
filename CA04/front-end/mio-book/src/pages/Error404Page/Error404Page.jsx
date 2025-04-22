import React from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import '../../stylesheets/General.css';
import '../../stylesheets/Error404.css';

function Error404Page() {
  return (
    <body>
        <Nav />

        <section class="error-section ">
            <div class="container">
                <div class="error-box col-sm-8 text-center">
                    <p class="error-code">404</p>
                    <div class="error-divider col-sm-2 mx-auto"></div>
                    <h1 class="mb-4">Page Not Found</h1>

                    <div class="bookshelf d-flex m-5 justify-content-center">
                        <div class="book"></div>
                        <div class="book"></div>
                        <div class="book"></div>
                        <div class="missing-book"></div>
                        <div class="book"></div>
                        <div class="book"></div>
                        <div class="book"></div>
                    </div>

                    <p class="lead mb-4">The book you're looking for is missing from our shelf.</p>
                    <p class="mb-5">It might have been borrowed, misplaced, or never existed. Let's find you something else to read.</p>

                    <div class="d-flex justify-content-center">
                        <a href="/" class="col-sm-6 green-btn btn">Return Home</a>
                    </div>
                </div>
            </div>
        </section>

        <Footer />
    </body>
  );
};

export default Error404Page;