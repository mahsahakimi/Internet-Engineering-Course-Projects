import React, { useEffect, useState } from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import EmptySpace from '../../components/EmptySpace';
import Review from '../../components/Review';
import '../../stylesheets/General.css';
import '../../stylesheets/Book.css';
import { useLocation } from 'react-router-dom';
import BookDetails from '../../components/BookDetails';
import SVGNoComments from '../../components/SVGNoComments';
import ModalAddReview from '../../components/ModalAddReview/ModalAddReview';





function BookPage() {

    const location = useLocation();
    const [bookDetails, setBookDetails] = useState(null);
    const [bookreviews, setBookreviews] = useState(null);
    const [error, setError] = useState(null);

    const getQueryParam = (key) => {
        const params = new URLSearchParams(location.search);
        return params.get(key);
    };

    const title = getQueryParam('title');

    const fetchBookDetails = async () => {
        if (!title) {
            setError('No book title provided in the URL.');
            return;
        }

        try {
            const response = await fetch(`/api/books/details?title=${title}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            const responseData = await response.json();
            if (responseData.status === 'success') {
                setBookDetails(responseData.data);
            } else {
                setError(responseData.message || 'Failed to fetch book details.');
            }
        } catch (err) {
            console.error('Fetch error:', err);
            setError('An error occurred while fetching book details.');
        }
    };
    const fetchReviews = async () => {        
        if (!title) {
            setError('No book title provided in the URL.');
            return;
        }
        try {
            const response = await fetch(`/api/books/reviews?title=${title}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            
            const responseData = await response.json();
            if (responseData.status === 'success') {
                
                setBookreviews(responseData.data);
            } else {
                setError(responseData.message || 'Failed to fetch book details.');
            }
        } catch (err) {
            console.error('Fetch error:', err);
            setError('An error occurred while fetching book details.');
        }
    };
    

    const [showRevModal, setshowRevModal] = useState(false);

    const handleOpenModal = () => setshowRevModal(true);
    const handleCloseModal = () => setshowRevModal(false);

    const handleReviewSubmit = async () => {
        fetchReviews();
    };

    useEffect(() => {
        fetchReviews();
        fetchBookDetails();
    }, []);
        
    return (
        <div>
            <Nav />
            <EmptySpace />
            <section className="d-flex justify-content-center">
                <BookDetails book={bookDetails}/>
            </section>


            <section className="d-flex justify-content-center">
                <div className="container bb white-box p-3 row col-sm-10 mt-5">
                    <div className="row mb-3">
                        <div className="col-sm-6">
                            <h5 className="fw-bold">Reviews <span className="fw-normal text-secondary">{bookreviews?.length}</span></h5>
                        </div>
                        <div className="col-sm-4"></div>
                        <div className="col-sm-2">
                            <button className="btn gray-btn col-sm-12" onClick={handleOpenModal}>
                                Add review <i class="bi bi-chat-square-text"></i>
                            </button>
                            {/* Modal */}
                            <ModalAddReview
                                show={showRevModal}
                                onClose={handleCloseModal}
                                onSubmit={handleReviewSubmit}
                                bookTitle={bookDetails?.title}
                            />
                        </div>
                    </div>
                    {bookreviews?.reviews?.length > 0 ? (
                    bookreviews.reviews.map((review, index) => (
                        <React.Fragment key={index}>
                        <Review br={review} />
                        <hr />
                        </React.Fragment>
                    ))
                    ) : (
                        <div className="empty-state">
                            <SVGNoComments />
                            <p className='text-secondary'>No comments yet.</p>
                        </div>
                    )}


                </div>
            </section>
            <Footer />
        </div>
  );
};

export default BookPage;