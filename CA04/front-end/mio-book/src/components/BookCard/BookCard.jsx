import {useNavigate, Link } from 'react-router-dom';
import '../../stylesheets/General.css';
import bookpng from '../../../src/images/book.png';
import StarRatingRenderer from '../StarRatingRenderer';


function BookCard({book}) {
    const navigate = useNavigate();
    const rating = book?.averageRating || 0;
    const gotobookpage = () => {
        navigate(`/book-details?title=${encodeURIComponent(book.title)}`);
    };

    return (
        <div className="book-card">
            <Link to={`/book-details?title=${encodeURIComponent(book?.title)}`}>
                <img src={bookpng} className="book-img book-cover col-12" alt="Book Cover" />
            </Link>            
            <div className="p-3 text-center">
                <Link to={`/book-details?title=${encodeURIComponent(book?.title)}`} className='link-text'>
                    <h5 className="mb-1">{book?.title}</h5>
                </Link>  
                <Link to={`/author?name=${encodeURIComponent(book?.author)}`} className='link-text'>
                    <p className="text-muted mb-2">{book?.author}</p>
                </Link>  
                
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
                onClick={gotobookpage}
                >Add to Cart</button>
                
            </div>
        </div>
    );
};

export default BookCard;