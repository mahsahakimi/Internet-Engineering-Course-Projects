import React from 'react';
import '../../stylesheets/General.css';
import book from '../../../src/images/book.png';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faStar as solidStar } from '@fortawesome/free-solid-svg-icons';
import { faStar as regularStar } from '@fortawesome/free-regular-svg-icons';

function BookCard() {
  return (
    <div className="book-card">
        <a href=""><img src={book} className="book-img book-cover col-12" alt="Book Cover" /></a>
        <div className="p-3 text-center">
            <h5 className="mb-1">Book Title</h5>
            <p className="text-muted mb-2">Author McName</p>
        </div>
        <div className="row">
        
            <div className="star-rating mb-2 col-7 d-flex justify-content-center">
                <FontAwesomeIcon icon={solidStar} className="fa-xs" />
                <FontAwesomeIcon icon={solidStar} className="fa-xs" />
                <FontAwesomeIcon icon={solidStar} className="fa-xs" />
                <FontAwesomeIcon icon={solidStar} className="fa-xs" />
                <FontAwesomeIcon icon={regularStar} className="fa-xs" />
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









