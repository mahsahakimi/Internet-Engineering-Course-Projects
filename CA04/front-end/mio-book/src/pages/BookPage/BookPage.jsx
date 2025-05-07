import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import Review from '../../components/Review';
import book from '../../../src/images/book.png';
import '../../stylesheets/General.css';
import '../../stylesheets/Book.css';

function BookPage() {
  const [bookDetails, setBookDetails] = useState(null);
  const [reviewsData, setReviewsData] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const bookTitleFromURL = 'The Shadow Hour'; // Hardcoded for now, consider making it dynamic
  const [currentPage, setCurrentPage] = useState(1);
  const reviewsPerPage = 4;
  const [totalReviews, setTotalReviews] = useState(0);

  useEffect(() => {
    const fetchBookData = async () => {
      setLoading(true);
      setBookDetails(null);
      setReviewsData(null);
      setCurrentPage(1);
      setTotalReviews(0);

      if (!bookTitleFromURL) {
        return;
      }

      try {
        // Fetch book details
        const detailsResponse = await fetch(
          `http://localhost:8080/api/books/details?title=${encodeURIComponent(bookTitleFromURL)}`
        );

        if (!detailsResponse.ok) {
          navigate('/error', { state: { message: detailsResponse.message } });
          return;
        }

        const detailsResponseJson = await detailsResponse.json();
        console.log('Book Details API Response:', detailsResponseJson);

        if (detailsResponseJson.status === 'success' && detailsResponseJson.data) {
          try {
            const parsedData = JSON.parse(detailsResponseJson.data);
            setBookDetails(parsedData);
          } catch (parseError) {
            console.error('Error parsing book details:', parseError);
            navigate('/error', { state: { message: 'Failed to parse book details.' } });
            return;
          }
        } else {
          navigate('/error', { state: { message: detailsResponseJson.message || 'Failed to retrieve book details.' } });
          return;
        }

        // Fetch book reviews
        const reviewsResponse = await fetch(
          `http://localhost:8080/api/books/reviews?title=${encodeURIComponent(
            bookTitleFromURL
          )}&page=${currentPage}&pageSize=${reviewsPerPage}`
        );

        if (!reviewsResponse.ok) {
          console.warn(`Failed to fetch book reviews: ${reviewsResponse.status}`);
          return;
        }

        const reviewsResponseJson = await reviewsResponse.json();
        console.log('Book Reviews API Response:', reviewsResponseJson);

        if (reviewsResponseJson.status === 'success' && reviewsResponseJson.data) {
          try {
            const parsedReviewsData = JSON.parse(reviewsResponseJson.data);
            setReviewsData(parsedReviewsData);
            // If totalReviews is not in the response, calculate from the fetched data
            setTotalReviews(parsedReviewsData.reviews ? parsedReviewsData.reviews.length : 0);
          } catch (parseError) {
            console.error('Error parsing book reviews:', parseError);
          }
        } else {
          console.warn(reviewsResponseJson.message || 'Failed to retrieve book reviews.');
        }
      } catch (err) {
        console.error('Error fetching data:', err);
        navigate('/error', { state: { message: 'Failed to load book data.' } });
      } finally {
        setLoading(false);
      }
    };

    fetchBookData();
  }, [bookTitleFromURL, navigate, currentPage, reviewsPerPage]);

  const handlePageChange = (newPage) => {
    setCurrentPage(newPage);
  };

  // Calculate totalPages based on the length of the fetched reviews (for the current page)
  const totalPages = Math.ceil(totalReviews / reviewsPerPage);
  const reviewsToDisplay = reviewsData?.reviews || [];

  if (loading) {
    return (
      <body>
        <Nav />
        <section className="d-flex justify-content-center">
          <div className="container white-box p-4 col-sm-10 row">
            <p>Loading book details and reviews...</p>
          </div>
        </section>
        <Footer />
      </body>
    );
  }

  if (!bookDetails) {
    return null;
  }

  const authorName = bookDetails.author?.name;
  const publisher = bookDetails.publisher;
  const year = bookDetails.year;
  const genres = bookDetails.genres ? bookDetails.genres.join(', ') : '';
  const synopsis = bookDetails.synopsis;
  const price = bookDetails.price ? (bookDetails.price / 100).toFixed(2) : '0.00';
  const title = bookDetails.title;
  const averageRating = reviewsData?.averageRating;

  return (
    <body>
      <Nav />
      <section className="d-flex justify-content-center">
        <div className="container white-box p-4 col-sm-10 row">
          <div className="col-sm-4 d-flex justify-content-center">
            <img src={book} className="book-main-img d-flex justify-content-center" alt="Book Cover" />
          </div>
          <div className="col-sm-8">
            <h2 className="book-title fw-bold mb-3">{title}</h2>

            <div className="row bright-titles mb-1">
              <div className="col">Author </div>
              <div className="col">Publisher </div>
              <div className="col">Year </div>
              <div className="col">Genre </div>
            </div>

            <div className="row mb-3">
              <div className="col">
                <Link to={`/author?name=${encodeURIComponent(authorName)}`} className="author-link">
                  {authorName}
                </Link>
              </div>
              <div className="col">{publisher}</div>
              <div className="col">{year}</div>
              <div className="col">{genres}</div>
            </div>

            <div className="bright-titles mb-1"> About </div>
            <div>
              {' '}
              <p className="">{synopsis}</p>
            </div>
            <div>
              {' '}
              <p className="fw-bold">${price}</p>{' '}
            </div>
            <button className="btn col-sm-4 green-btn ">Add to Cart</button>
          </div>
        </div>
      </section>

      <section className="d-flex justify-content-center">
        <div className="container white-box p-3 row col-sm-10 mt-5">
          <div className="row mb-3">
            <div className="col-sm-6">
              <h5 className="fw-bold">
                Reviews <span className="fw-normal text-secondary">{reviewsData?.reviews?.length || 0}</span>
              </h5>
            </div>
            <div className="col-sm-4"></div>
            <div className="col-sm-2">
              <button className="btn gray-btn col-sm-12">
                Add reviews <i className="fas fa-list-ul ms-2"></i>
              </button>
            </div>
          </div>
          {reviewsToDisplay.map((review, index) => (
            <React.Fragment key={index}>
              <Review review={review} />
              {index < reviewsToDisplay.length - 1 && <hr />}
            </React.Fragment>
          ))}

          {reviewsData?.reviews?.length > reviewsPerPage && (
            <div className="pg-container">
              <nav aria-label="Page navigation">
                <ul className="pagination">
                  <li className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
                    <button className="page-link" onClick={() => handlePageChange(currentPage - 1)} aria-label="Previous">
                      &lsaquo;
                    </button>
                  </li>
                  {/* We can't accurately display all page numbers without totalReviews */}
                  {/* For a basic "Next" and "Previous" only pagination based on fetched data: */}
                  <li className={`page-item ${reviewsData?.reviews?.length < reviewsPerPage ? 'disabled' : ''}`}>
                    <button className="page-link" onClick={() => handlePageChange(currentPage + 1)} aria-label="Next">
                      &rsaquo;
                    </button>
                  </li>
                </ul>
              </nav>
            </div>
          )}
        </div>
      </section>
      <Footer />
    </body>
  );
}

export default BookPage;