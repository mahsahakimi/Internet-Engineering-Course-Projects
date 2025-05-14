import React from 'react';
import '../../stylesheets/General.css';
import bookpng from '../../../src/images/book.png';
import { Link } from 'react-router-dom';
import StarRatingRenderer from '../StarRatingRenderer';


function BookCard({book}) {

    const rating = book?.averageRating || 0;

    return (
        <div className="book-card">
            <Link to={`/book-details?title=${encodeURIComponent(book?.title)}`}>
                <img src={bookpng} className="book-img book-cover col-12" alt="Book Cover" />
            </Link>            
            <div className="p-3 text-center">
                <Link to={`/book-details?title=${encodeURIComponent(book?.title)}`} className='link-text'>
                    <h5 className="mb-1">{book?.title}</h5>
                </Link>  
                <p className="text-muted mb-2">{book?.author}</p>
            </div>
            <div className="row align-items-center mb-3 px-3">
                <div className="col-8">
                    {<StarRatingRenderer rating={rating}/>}
                </div>
                <div className="col-4">
                    <span className="fs-6">${book?.price}</span>
                </div>
            </div>
            <div className="row justify-content-center">
                <button 
                className="green-btn col-10 mb-2 "
                // onClick={handleOpenModal}
                >Add to Cart</button>
                {/* <ModalAddToCart
                    show={showCartModal}
                    onClose={handleCloseModal}
                    bookTitle={book?.title}
                    bookPrice={book?.price}
                    updateBookStatus={setBookstatus}
                /> */}
            </div>
        </div>
    );
};

export default BookCard;