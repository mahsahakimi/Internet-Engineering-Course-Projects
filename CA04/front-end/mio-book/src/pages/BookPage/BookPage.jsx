import React, { useEffect, useState } from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import EmptySpace from '../../components/EmptySpace';
import Review from '../../components/Review';
import '../../stylesheets/General.css';
import '../../stylesheets/Book.css';
import { useLocation } from 'react-router-dom';
import BookDetails from '../../components/BookDetails';

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
                            <button className="btn gray-btn col-sm-12s">
                                Add reviews  
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-chat-left-text" viewBox="0 0 16 16">
                                    <path d="M14 1a1 1 0 0 1 1 1v8a1 1 0 0 1-1 1H4.414A2 2 0 0 0 3 11.586l-2 2V2a1 1 0 0 1 1-1zM2 0a2 2 0 0 0-2 2v12.793a.5.5 0 0 0 .854.353l2.853-2.853A1 1 0 0 1 4.414 12H14a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z"/>
                                    <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5"/>
                                </svg>
                                </button>
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
                    <p>No reviews yet.</p>
                    )}

                    {/* <div className="pg-container">
                        <nav aria-label="Page navigation">
                            <ul className="pagination">
                                <li className="page-item"><a className="page-link" href="#" aria-label="Previous">&lsaquo;</a></li>
                                <li className="page-item active"><a className="page-link" href="#">1</a></li>
                                <li className="page-item"><a className="page-link" href="#">2</a></li>
                                <li className="page-item"><a className="page-link" href="#">3</a></li>
                                <li className="page-item"><a className="page-link" href="#">4</a></li>
                                <li className="page-item"><a className="page-link" href="#">5</a></li>
                                <li className="page-item"><a className="page-link" href="#" aria-label="Next">&rsaquo;</a>
                                </li>
                            </ul>
                        </nav>
                    </div> */}
                </div>
            </section>
            <Footer />
        </div>
  );
};

export default BookPage;