import React, {useState, useEffect} from 'react';
import { useNavigate } from 'react-router-dom';
import bookPNG from '../../../src/images/book.png';
import StarRatingRenderer from '../StarRatingRenderer';
import ModalAddToCart from '../ModalAddToCart/ModalAddToCart';


function BookDetails({book}) {
    const [showCartModal, setshowCartModal] = useState(false);
    const [bookstatus, setBookstatus] = useState(null);
    const navigate = useNavigate();
    const localUser = JSON.parse(localStorage.getItem('activeUser'));
    const handleOpenModal = () => setshowCartModal(true);
    const handleCloseModal = () => setshowCartModal(false);

    const fetchBookStatus = async () => {
        if (!book?.title) {
            console.error('No book title provided to fetch status.');
            return;
        }
        
        if(localUser?.userInfo?.role !== "admin"){
            try {
                const response = await fetch(`/api/users/book_status?title=${book?.title}`, {
                    method: 'GET',
                    headers: {
                        'Session-ID': localUser.userInfo.sessionId,
                    }
                });

                const responseData = await response.json();
                if (responseData.status === 'success') {
                    setBookstatus(responseData.data);
                } else {
                    console.error(`Failed to fetch book status: ${responseData.message}`);
                    navigate('/error', { state: { message: responseData.message || 'Failed to fetch book status.' } });
                }
            } catch (err) {
                console.error('Fetch error:', err);
                navigate('/error', { state: { message: 'An error occurred while fetching book status.' } });
            }
        }
        
    }

    useEffect(() => {
        if (book?.title) {
            fetchBookStatus();
        }
    }, [book, navigate]);

    return (
        <div className="container white-box bb p-4 col-sm-10 row">
            <div className="col-sm-4 d-flex justify-content-center position-relative">
                <img src={bookPNG} className="book-main-img d-flex justify-content-center" alt="Book Cover" />
                {bookstatus && (
                    <span className="badge bg-success position-absolute bottom-0 start-50 translate-middle-x mb-2">
                        {bookstatus}
                    </span>
                )}
            </div>

            <div className="col-sm-8">
                <h2 className="book-title fw-bold">{book?.title}</h2>
                <div className="d-flex align-items-center gap-4  mb-4">
                    <StarRatingRenderer rating={book?.averageRating}/>  {book?.averageRating}
                </div>
                <div className="row bright-titles">
                    <div className="col">Author </div>
                    <div className="col">Publisher </div>
                    <div className="col">Year </div>
                    <div className="col">Genre </div>
                </div>

                <div className="row mb-3">
                    <div className="col">{book?.author}</div>
                    <div className="col">{book?.publisher}</div>
                    <div className="col">{book?.year}</div>
                    <div className="col">{book?.genres?.join(", ")}</div>
                </div>

                <div className="bright-titles"> About </div>
                <div> <p className="">        {book?.synopsis}        </p></div>
                <div> <p className="fw-bold">${book?.price}</p> </div>
                <button
                    className={`btn col-sm-4 ${bookstatus || localUser?.userInfo?.role === "admin" ? 'gray-btn' : 'green-btn'}`}
                    onClick={handleOpenModal}
                    disabled={!!bookstatus || localUser?.userInfo?.role === "admin"} 
                    >
                    Add to Cart
                </button>
                <ModalAddToCart
                    show={showCartModal}
                    onClose={handleCloseModal}
                    bookTitle={book?.title}
                    bookPrice={book?.price}
                    updateBookStatus={setBookstatus}
                />
            </div>
        </div>
    );
};

export default BookDetails;