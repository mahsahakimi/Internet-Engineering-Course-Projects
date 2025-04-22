import React from 'react';
import '../../stylesheets/General.css';
import book from '../../../public/book.png';

function BookCard() {
  return (
    <div className="book-card">
        <a href=""><img src={book} className="book-img book-cover col-12" alt="Book Cover" /></a>
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
  );
};

export default BookCard;









