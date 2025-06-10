import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../../stylesheets/Modal.css';
import { UserContext } from '../../UserContex';

const ModalAddReview = ({ show, onClose, onSubmit, bookTitle }) => {
    const { user, setUser } = useContext(UserContext);
    const [rating, setRating] = useState(0);
    const [reviewText, setReviewText] = useState('');
    const navigate = useNavigate();

    const handleRating = (value) => setRating(value);
    const onCloseHere = () => {
        setRating(0);
        setReviewText('');
        onClose();
    }

    const handleSubmit = async () => {
      try {
          const response = await fetch('/api/books/add_review', {
              method: 'POST',
              headers: {
                'Authorization': `Bearer ${user.userInfo.token}`,
                'Content-Type': 'application/json',
              },
              body: JSON.stringify({
                title: bookTitle,
                rate: rating,
                comment: reviewText,
              }),
          });

          if (!response.ok) {
              console.error(`Failed to add review: ${response.status}`);
              const responseData = await response.json();
              navigate('/error', { state: { message: responseData.message } });
              return;
          }

          const responseData = await response.json();
          if (responseData.status === 'success') {
              onSubmit();
          } else {
              console.error(responseData.message || 'Failed to add review.');
              navigate('/error', { state: { message: responseData.message || 'Failed to add review.' } });
          }
        } catch (error) {
            console.error('Submission error:', error);
            navigate('/error', { state: { message: 'An error occurred while adding the review.' } });
        }
        setRating(0);
        setReviewText('');
        onClose();
    };

    if (!show) return null;

    return (
            <>
                <div className="modal fade show d-block" tabIndex="-1" role="dialog">
                    <div className="modal-dialog modal-dialog-centered" role="document">
                        <div className="modal-content">
                          <div className="modal-header d-flex justify-content-between">
                            <div className="flex-grow-1 text-center">
                              <h6 className="modal-title">Add Review</h6>
                            </div>
                            <button type="button" className="btn-close" aria-label="Close" style={{ transform: 'scale(0.8)' }} onClick={onCloseHere}></button>

                          </div>
                          <div className="modal-body">
                            <p className="mb-1">Rating:</p>
                            <div className="mb-3 d-flex justify-content-between">
                              {[1, 2, 3, 4, 5].map((star) => (
                                <span
                                  key={star}
                                  style={{ cursor: 'pointer', fontSize: '1.8rem' }}
                                  onClick={() => handleRating(star)}
                                >
                                  <i className={`bi ${rating >= star ? 'bi-star-fill' : 'bi-star'}`}></i>
                                </span>
                              ))}
                            </div>


                            <textarea
                              className="form-control"
                              rows="6"
                              placeholder="Write your review here..."
                              value={reviewText}
                              onChange={(e) => setReviewText(e.target.value)}
                            />
                          </div>

                          <div className="d-flex flex-column align-items-center w-100">
                          <button
                              className={`btn ${rating === 0 || reviewText.trim() === '' ? 'gray-btn' : 'green-btn'} mb-3 col-12`}
                              onClick={handleSubmit}
                              disabled={rating === 0 || reviewText.trim() === ''}
                          >
                              Submit Review
                          </button>
                          <button className="btn gray-btn col-12" onClick={onCloseHere}>
                              Cancel
                          </button>


                        </div>
                        </div>
                      </div>
                    </div>
                {/* Backdrop */}
                <div className="modal-backdrop fade show"></div>
            </>
        );
    };

    export default ModalAddReview;