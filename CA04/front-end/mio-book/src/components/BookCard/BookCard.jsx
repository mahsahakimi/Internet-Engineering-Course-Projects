import React from 'react';
import '../../stylesheets/General.css';
import bookPNG from '../../../src/images/book.png';
import { Link } from 'react-router-dom';
import StarRatingRenderer from '../StarRatingRenderer';

function BookCard({ book }) {
    const {
        title = 'Untitled Book',
        author = 'Unknown Author',
        price = 0.00,
        averageRating = 0
    } = book;

    return (
        <div className="book-card">
            <Link to={`/book-details?title=${encodeURIComponent(title)}`}>
                <img
                    src={bookPNG}
                    className="book-img book-cover col-12"
                    alt={title || 'Book cover'}
                />
            </Link>
            <div className="p-3 text-center">
                <Link to={`/book-details?title=${encodedTitle}`}>
                    <h5 className="mb-1">{title}</h5>
                </Link>
                <p className="text-muted mb-2">{author}</p>
            </div>
            <div className="row align-items-center mb-3 px-3">
                <div className="col-8">
                    <StarRatingRenderer rating={averageRating} />
                </div>
                <div className="col-4">
                    <span className="fs-6">${price.toFixed(2)}</span>
                </div>
            </div>
            <div className="row justify-content-center">
                <button className="green-btn col-10 mb-2">Add to Cart</button>
            </div>
        </div>
    );
}

export default BookCard;