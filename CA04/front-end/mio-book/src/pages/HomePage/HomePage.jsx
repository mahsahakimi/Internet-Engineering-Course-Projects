import React from 'react';
import Footer from '../../components/Footer';
import BookCard from '../../components/BookCard';
import Nav from '../../components/Nav';
import book from '../../../src/images/book.png';
import '../../stylesheets/HomePage.css';
import '../../stylesheets/General.css';


function HomePage() {
    return (
        <body>
            <Nav />

            <section className="intro-section py-5 mb-5">
                <div className="container">
                    <div className="row">
                        <div className="col-sm-1"> </div>
                        <div className="col-sm-5">
                            <h2 className="mb-3">Welcome to MioBook – the online bookstore where you can buy or borrow books with ease.</h2>
                            <p>Whether you're looking for the latest bestseller, a classNameic novel, or a niche title, MioBook has you covered.</p>
                            <p>Here, you can quickly find books by title, author, and genre. And if you're not sure to buy? Try borrowing instead! Rent a book for just a fraction of the price and enjoy full access for a set period.</p>
                            <p>Your next great read is just a click away. Visit MioBook today and let the perfect book find you! 📚✨</p>
                        </div>
                        <div className="col-sm-1"> </div>
                        <div className="col-sm-4">
                            <img src={book} alt="Bookshelf" className="main-home-image img-fluid" />
                        </div>
                        <div className="col-sm-1"> </div>
                    </div>
                </div>
            </section>

            <section className="container mb-5">
                <h2 className="mb-4">New Releases</h2>
                <div className="row row-cols-1 row-cols-sm-5 ">
                    <div className="col">
                        <BookCard />
                    </div>

                    <div className="col">
                        <BookCard />
                    </div>

                    <div className="col">
                        <BookCard />
                    </div>

                    <div className="col">
                        <BookCard />
                    </div>

                    <div className="col">
                        <BookCard />
                    </div>
                </div>
            </section>


            <section className="container mb-5">
                <h2 className="mb-4">Related</h2>
                <div className="row row-cols-1 row-cols-sm-5 ">
                    <div className="col">
                        <BookCard />
                    </div>

                    <div className="col">
                        <BookCard />
                    </div>

                    <div className="col">
                        <BookCard />
                    </div>

                    <div className="col">
                        <BookCard />
                    </div>

                    <div className="col">
                        <BookCard />
                    </div>
                </div>
            </section>

            <Footer />
        </body>
    );
};

export default HomePage;