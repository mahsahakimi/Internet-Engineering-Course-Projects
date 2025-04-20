import React from 'react';

const X = () => {
    return (
        <>
            <nav className="navbar bg-white shadow-sm mb-5 ">
                <div className="container-fluid d-flex">
                    <a className="navbar-brand d-flex" href="#">
                        <div className="logo-container ms-5">
                            <img src="../images/LOGO.png" alt="Meow Logo" className="logo-cover" />
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
                    <button className="green-btn ">Buy now</button>
                </div>
            </nav>


            <section className="intro-section py-5 mb-5">
                <div className="container">
                    <div className="row">
                        <div className="col-sm-1"> </div>
                        <div className="col-sm-5">
                            <h2 className="mb-3">Welcome to MioBook – the online bookstore where you can buy or borrow books with ease.</h2>
                            <p>Whether you're looking for the latest bestseller, a classic novel, or a niche title, MioBook has you covered.</p>
                            <p>Here, you can quickly find books by title, author, and genre. And if you're not sure to buy? Try borrowing instead! Rent a book for just a fraction of the price and enjoy full access for a set period.</p>
                            <p>Your next great read is just a click away. Visit MioBook today and let the perfect book find you! 📚✨</p>
                        </div>
                        <div className="col-sm-1"> </div>
                        <div className="col-sm-4">
                            <img src="../images/book.png" alt="Bookshelf" className="main-home-image img-fluid" />
                        </div>
                        <div className="col-sm-1"> </div>
                    </div>
                </div>
            </section>

            <section className="container mb-5">
                <h2 className="mb-4">New Releases</h2>
                <div className="row row-cols-1 row-cols-sm-5 ">
                    <div className="col">
                        <div className="book-card">
                            <a href=""><img src="../images/book.png" className="book-img book-cover col-12" alt="Book Cover" /></a>
                            <div className="p-3 text-center">
                                <h5 className="mb-1">Book Title</h5>
                                <p className="text-muted mb-2">Author McName</p>
                            </div>
                            <div className="row">
                                <div className="star-rating mb-2 col-7">
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="far fa-star fa-xs"></i>
                                </div>
                                <p className="col-5">$10.25</p>
                            </div>
                            <div className="row justify-content-center">
                                <button className="green-btn col-10 mb-2 ">Add to Cart</button>
                            </div>
                        </div>
                    </div>

                    <div className="col">
                        <div className="book-card">
                            <a href=""><img src="../images/book.png" className="book-img book-cover col-12" alt="Book Cover" /></a>
                            <div className="p-3 text-center">
                                <h5 className="mb-1">Book Title</h5>
                                <p className="text-muted mb-2">Author McName</p>
                            </div>
                            <div className="row">
                                <div className="star-rating mb-2 col-7">
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="far fa-star fa-xs"></i>
                                </div>
                                <p className="col-5">$10.25</p>
                            </div>
                            <div className="row justify-content-center">
                                <button className="green-btn col-10 mb-2 ">Add to Cart</button>
                            </div>
                        </div>
                    </div>

                    <div className="col">
                        <div className="book-card">
                            <a href=""><img src="../images/book.png" className="book-img book-cover col-12" alt="Book Cover" /></a>
                            <div className="p-3 text-center">
                                <h5 className="mb-1">Book Title</h5>
                                <p className="text-muted mb-2">Author McName</p>
                            </div>
                            <div className="row">
                                <div className="star-rating mb-2 col-7">
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="far fa-star fa-xs"></i>
                                </div>
                                <p className="col-5">$10.25</p>
                            </div>
                            <div className="row justify-content-center">
                                <button className="green-btn col-10 mb-2 ">Add to Cart</button>
                            </div>
                        </div>
                    </div>

                    <div className="col">
                        <div className="book-card">
                            <a href=""><img src="../images/book.png" className="book-img book-cover col-12" alt="Book Cover" /></a>
                            <div className="p-3 text-center">
                                <h5 className="mb-1">Book Title</h5>
                                <p className="text-muted mb-2">Author McName</p>
                            </div>
                            <div className="row">
                                <div className="star-rating mb-2 col-7">
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="far fa-star fa-xs"></i>
                                </div>
                                <p className="col-5">$10.25</p>
                            </div>
                            <div className="row justify-content-center">
                                <button className="green-btn col-10 mb-2 ">Add to Cart</button>
                            </div>
                        </div>
                    </div>

                    <div className="col">
                        <div className="book-card">
                            <a href=""><img src="../images/book.png" className="book-img book-cover col-12" alt="Book Cover" /></a>
                            <div className="p-3 text-center">
                                <h5 className="mb-1">Book Title</h5>
                                <p className="text-muted mb-2">Author McName</p>
                            </div>
                            <div className="row">
                                <div className="star-rating mb-2 col-7">
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="far fa-star fa-xs"></i>
                                </div>
                                <p className="col-5">$10.25</p>
                            </div>
                            <div className="row justify-content-center">
                                <button className="green-btn col-10 mb-2 ">Add to Cart</button>
                            </div>
                        </div>
                    </div>
                </div>
            </section>


            <section className="container mb-5">
                <h2 className="mb-4">Related</h2>
                <div className="row row-cols-1 row-cols-sm-5 ">
                    <div className="col">
                        <div className="book-card">
                            <a href=""><img src="../images/book.png" className="book-img book-cover col-12" alt="Book Cover" /></a>
                            <div className="p-3 text-center">
                                <h5 className="mb-1">Book Title</h5>
                                <p className="text-muted mb-2">Author McName</p>
                            </div>
                            <div className="row">
                                <div className="star-rating mb-2 col-7">
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="far fa-star fa-xs"></i>
                                </div>
                                <p className="col-5">$10.25</p>
                            </div>
                            <div className="row justify-content-center">
                                <button className="green-btn col-10 mb-2 ">Add to Cart</button>
                            </div>
                        </div>
                    </div>

                    <div className="col">
                        <div className="book-card">
                            <a href=""><img src="../images/book.png" className="book-img book-cover col-12" alt="Book Cover" /></a>
                            <div className="p-3 text-center">
                                <h5 className="mb-1">Book Title</h5>
                                <p className="text-muted mb-2">Author McName</p>
                            </div>
                            <div className="row">
                                <div className="star-rating mb-2 col-7">
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="far fa-star fa-xs"></i>
                                </div>
                                <p className="col-5">$10.25</p>
                            </div>
                            <div className="row justify-content-center">
                                <button className="green-btn col-10 mb-2 ">Add to Cart</button>
                            </div>
                        </div>
                    </div>

                    <div className="col">
                        <div className="book-card">
                            <a href=""><img src="../images/book.png" className="book-img book-cover col-12" alt="Book Cover" /></a>
                            <div className="p-3 text-center">
                                <h5 className="mb-1">Book Title</h5>
                                <p className="text-muted mb-2">Author McName</p>
                            </div>
                            <div className="row">
                                <div className="star-rating mb-2 col-7">
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="far fa-star fa-xs"></i>
                                </div>
                                <p className="col-5">$10.25</p>
                            </div>
                            <div className="row justify-content-center">
                                <button className="green-btn col-10 mb-2 ">Add to Cart</button>
                            </div>
                        </div>
                    </div>

                    <div className="col">
                        <div className="book-card">
                            <a href=""><img src="../images/book.png" className="book-img book-cover col-12" alt="Book Cover" /></a>
                            <div className="p-3 text-center">
                                <h5 className="mb-1">Book Title</h5>
                                <p className="text-muted mb-2">Author McName</p>
                            </div>
                            <div className="row">
                                <div className="star-rating mb-2 col-7">
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="far fa-star fa-xs"></i>
                                </div>
                                <p className="col-5">$10.25</p>
                            </div>
                            <div className="row justify-content-center">
                                <button className="green-btn col-10 mb-2 ">Add to Cart</button>
                            </div>
                        </div>
                    </div>

                    <div className="col">
                        <div className="book-card">
                            <a href=""><img src="../images/book.png" className="book-img book-cover col-12" alt="Book Cover" /></a>
                            <div className="p-3 text-center">
                                <h5 className="mb-1">Book Title</h5>
                                <p className="text-muted mb-2">Author McName</p>
                            </div>
                            <div className="row">
                                <div className="star-rating mb-2 col-7">
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="fas fa-star fa-xs"></i>
                                    <i className="far fa-star fa-xs"></i>
                                </div>
                                <p className="col-5">$10.25</p>
                            </div>
                            <div className="row justify-content-center">
                                <button className="green-btn col-10 mb-2 ">Add to Cart</button>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <footer className="w-100">
                <p className="text-center">Copyright © 2025 - MioBook</p>
            </footer>
        </>
    );
};

export default X;
