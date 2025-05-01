import React from 'react';
import '../../stylesheets/General.css';
import bookpng from '../../../src/images/book.png';
import SVGStar from '../../components/SVGStar';
import SVGStarFill from '../../components/SVGStarFill';
import SVGStarHalfFill from '../SVGStarHalfFill';
import { Link } from 'react-router-dom';


function BookCard({book}) {

    const rating = book?.averageRating || 0;
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating - fullStars >= 0.25 && rating - fullStars < 0.75;
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

    const renderStars = () => {
        const stars = [];

        for (let i = 0; i < fullStars; i++) {
            stars.push(<SVGStarFill size="14" />);
        }
        if (hasHalfStar) {
            stars.push(<SVGStarHalfFill size="14" />);
        }
        for (let i = 0; i < emptyStars; i++) {
            stars.push(<SVGStar size="14" />);
        }
        return stars;
    };

    return (
        <div className="book-card">
            <Link to={`/book-details?title=${encodeURIComponent(book?.title)}`}>
                <img src={bookpng} className="book-img book-cover col-12" alt="Book Cover" />
            </Link>            
            <div className="p-3 text-center">
                <h5 className="mb-1">{book?.title}</h5>
                <p className="text-muted mb-2">{book?.author}</p>
            </div>
            <div className="row align-items-center mb-3 px-3">
                <div className="col-8">
                    {renderStars()}
                </div>
                <div className="col-4">
                    <span className="fs-6">${book?.price}</span>
                </div>
            </div>
            <div className="row justify-content-center">
                <button className="green-btn col-10 mb-2 ">Add to Cart</button>
            </div>
        </div>
    );
};

export default BookCard;
