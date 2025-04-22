import React from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import Review from '../../components/Review';
import book from '../../../src/images/book.png';
import '../../stylesheets/General.css';
import '../../stylesheets/Book.css';

function BookPage() {
  return (
    <body>
        <Nav />
        <section className="d-flex justify-content-center">
            <div className="container white-box p-4 col-sm-10 row">
                <div className="col-sm-4 d-flex justify-content-center">
                    <img src={book} className="book-main-img d-flex justify-content-center" alt="Book Cover" />
                </div>
                <div className="col-sm-8">
                    <h2 className="book-title fw-bold mb-3">Book Title</h2>

                    <div className="row bright-titles mb-1">
                        <div className="col">Author </div>
                        <div className="col">Publisher </div>
                        <div className="col">Year </div>
                    </div>

                    <div className="row mb-3">
                        <div className="col">Author McName </div>
                        <div className="col">The publishers </div>
                        <div className="col">2025 </div>
                    </div>

                    <div className="bright-titles mb-1"> About </div>
                    <div> <p className="">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.        </p>
                    </div>
                    <div> <p className="fw-bold">$18.99</p> </div>
                    <button className="btn col-sm-4 green-btn ">Add to Cart</button>
                </div>
            </div>
        </section>



        <section className="d-flex justify-content-center">
            <div className="container white-box p-3 row col-sm-10 mt-5">
                <div className="row mb-3">
                    <div className="col-sm-6">
                        <h5 className="fw-bold">Reviews <span className="fw-normal text-secondary">130</span></h5>
                    </div>
                    <div className="col-sm-4"></div>
                    <div className="col-sm-2">
                        <button className="btn gray-btn col-sm-12">
                            Add reviews <i className="fas fa-list-ul ms-2"></i>
                        </button>
                    </div>
                </div>
                <Review />
                <hr />
                <Review />
                <hr />
                <Review />
                <hr />
                <Review />

                <div className="pg-container">
                    <nav aria-label="Page navigation">
                        <ul className="pagination">
                            <li className="page-item"><a className="page-link" href="#" aria-label="Previous">&lsaquo;</a></li>
                            <li className="page-item active"><a className="page-link" href="#">1</a></li>
                            <li className="page-item"><a className="page-link" href="#">2</a></li>
                            <li className="page-item"><a className="page-link" href="#">3</a></li>
                            <li className="page-item"><a className="page-link" href="#">4</a></li>
                            <li className="page-item"><a className="page-link" href="#">5</a></li>
                            <li className="page-item"><a className="page-link" href="#" aria-label="Next">&rsaquo;</a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </section>
        <Footer />
    </body>
  );
};

export default BookPage;