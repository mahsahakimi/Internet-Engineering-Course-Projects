import React, { useState, useEffect, useContext } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import EmptySpace from '../../components/EmptySpace';
import Review from '../../components/Review';
import '../../stylesheets/General.css';
import '../../stylesheets/Book.css';
import BookDetails from '../../components/BookDetails';
import SVGNoComments from '../../components/SVGNoComments';
import ModalAddReview from '../../components/ModalAddReview/ModalAddReview';
import { UserContext } from '../../UserContex';

function BookPage() {
    const { user, setUser } = useContext(UserContext);
    const location = useLocation();
    const navigate = useNavigate();
    const [bookDetails, setBookDetails] = useState(null);
    const [bookreviews, setBookreviews] = useState(null);
    const [reviewPage, setReviewPage] = useState(1);

    const getQueryParam = (key) => {
        const params = new URLSearchParams(location.search);
        return params.get(key);
    };

    const title = getQueryParam('title');

    const fetchBookDetails = async () => {
        if (!title) {
            console.error('No book title provided in the URL.');
            return;
        }

        try {
            const response = await fetch(`/api/books/details?title=${title}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${user.userInfo.token}`,
                    'Content-Type': 'application/json',
                },
            });

            const responseData = await response.json();
            if (responseData.status === 'success') {
                setBookDetails(responseData.data);
            } else {
                console.error(responseData.message || 'Failed to fetch book details.');
                navigate('/error', { state: { message: responseData.message || 'Failed to fetch book details.' } });
            }
        } catch (err) {
            console.error('Fetch error:', err);
            navigate('/error', { state: { message: err.message } });
        }
    };

    const fetchReviews = async (page = reviewPage) => {
        if (!title) {
            console.error('No book title provided in the URL.');
            return;
        }
        try {
            const response = await fetch(`/api/books/reviews?title=${encodeURIComponent(title)}&page=${page}&pageSize=3`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${user.userInfo.token}`,
                    'Content-Type': 'application/json',
                },
            });

            const responseData = await response.json();
            if (responseData.status === 'success') {
                setBookreviews(responseData.data);
            } else {
                console.warn(responseData.message || 'Failed to fetch book reviews.');
            }
        } catch (err) {
            console.error('Fetch error:', err);
        }
    };

    const [showRevModal, setshowRevModal] = useState(false);

    const handleOpenModal = () => setshowRevModal(true);
    const handleCloseModal = () => setshowRevModal(false);

    const handleReviewSubmit = async () => {
        setReviewPage(1);
        fetchReviews(1);
    };

    useEffect(() => {
        setReviewPage(1);
    }, [title]);

    useEffect(() => {
        if (!user) return;
        fetchBookDetails();
    }, [title, navigate, user]);

    useEffect(() => {
        if (!user) return;
        fetchReviews(reviewPage);
    }, [title, user, reviewPage]);

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
                            <button 
                                className="btn gray-btn col-sm-12" 
                                onClick={handleOpenModal}
                                disabled={user?.userInfo?.role === "admin"}
                                >
                                Add review <i className="bi bi-chat-square-text"></i>
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
                        <>
                            {bookreviews.reviews.map((review, index) => (
                                <React.Fragment key={index}>
                                    <Review br={review} />
                                    <hr />
                                </React.Fragment>
                            ))}
                            {bookreviews.totalPages > 1 && (
                                <nav aria-label="Reviews pagination" className="d-flex justify-content-center mt-3">
                                    <ul className="pagination mb-0">
                                        <li className={`page-item ${reviewPage <= 1 ? 'disabled' : ''}`}>
                                            <button
                                                type="button"
                                                className="page-link"
                                                onClick={() => setReviewPage((prev) => Math.max(1, prev - 1))}
                                                disabled={reviewPage <= 1}
                                            >
                                                &laquo;
                                            </button>
                                        </li>
                                        {Array.from({ length: bookreviews.totalPages }, (_, i) => i + 1).map((pageNum) => (
                                            <li key={pageNum} className={`page-item ${reviewPage === pageNum ? 'active' : ''}`}>
                                                <button
                                                    type="button"
                                                    className="page-link"
                                                    onClick={() => setReviewPage(pageNum)}
                                                >
                                                    {pageNum}
                                                </button>
                                            </li>
                                        ))}
                                        <li className={`page-item ${reviewPage >= bookreviews.totalPages ? 'disabled' : ''}`}>
                                            <button
                                                type="button"
                                                className="page-link"
                                                onClick={() => setReviewPage((prev) => Math.min(bookreviews.totalPages, prev + 1))}
                                                disabled={reviewPage >= bookreviews.totalPages}
                                            >
                                                &raquo;
                                            </button>
                                        </li>
                                    </ul>
                                </nav>
                            )}
                        </>
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
}

export default BookPage;