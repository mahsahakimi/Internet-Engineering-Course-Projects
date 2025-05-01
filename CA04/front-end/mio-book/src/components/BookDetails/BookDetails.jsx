import React from 'react';
import bookpng from '../../../src/images/book.png';
import StarRatingRenderer from '../StarRatingRenderer';


function BookDetails({book}) {


    return (
        <div className="container white-box bb p-4 col-sm-10 row">
            <div className="col-sm-4 d-flex justify-content-center">
                <img src={bookpng} className="book-main-img d-flex justify-content-center" alt="Book Cover" />
            </div>
            <div className="col-sm-8">
                <h2 className="book-title fw-bold">{book?.title}</h2>
                <div className="d-flex align-items-center gap-4  mb-4"> 
                    <StarRatingRenderer rating={book?.averageRating} size={15}/>  {book?.averageRating}
                </div>
                <div className="row bright-titles">
                    <div className="col">Author </div>
                    <div className="col">Publisher </div>
                    <div className="col">Year </div>
                    <div className="col">Genre </div>
                </div>

                <div className="row mb-3">
                    <div className="col">{book?.author?.name}</div>
                    <div className="col">{book?.publisher}</div>
                    <div className="col">{book?.year}</div>
                    <div className="col">{book?.genres}</div>
                </div>

                <div className="bright-titles"> About </div>
                <div> <p className="">        {book?.synopsis}        </p></div>
                <div> <p className="fw-bold">${book?.price}</p> </div>
                <button className="btn col-sm-4 green-btn ">Add to Cart</button>
            </div>
        </div>
    );
};

export default BookDetails;