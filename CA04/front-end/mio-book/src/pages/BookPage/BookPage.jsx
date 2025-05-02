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

    const [showModal, setShowModal] = useState(false);

    const handleOpenModal = () => setShowModal(true);
    const handleCloseModal = () => setShowModal(false);

    const handleReviewSubmit = async ({ rating, reviewText }) => {
        try {
        const res = await fetch('/api/books/review', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title, rating, review: reviewText })
        });

        const result = await res.json();
        if (result.status === 'success') {
            fetchReviews(); // refresh reviews after submitting
        }
        } catch (err) {
        console.error('Review submit error:', err);
        }
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
                                Add review
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-chat-left-text" viewBox="0 0 16 16">
                                    <path d="M14 1a1 1 0 0 1 1 1v8a1 1 0 0 1-1 1H4.414A2 2 0 0 0 3 11.586l-2 2V2a1 1 0 0 1 1-1zM2 0a2 2 0 0 0-2 2v12.793a.5.5 0 0 0 .854.353l2.853-2.853A1 1 0 0 1 4.414 12H14a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z"/>
                                    <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5"/>
                                </svg>
                            </button>
                            {/* Modal */}
                            <ModalAddReview
                                show={showModal}
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