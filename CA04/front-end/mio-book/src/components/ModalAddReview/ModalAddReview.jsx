import React, { useState } from 'react';
import SVGStar from '../SVGStar';
import SVGStarFill from '../SVGStarFill';
import '../../stylesheets/Modal.css';
import bookPNG from '../../images/book.png'

const ReviewModal = ({ show, onClose, bookTitle, bookCover }) => {
  const [rating, setRating] = useState(2);
  const [reviewText, setReviewText] = useState('');

  if (!show) return null;

  const handleSubmit = () => {
    console.log({
      title: bookTitle,
      rating,
      review: reviewText
    });

    setRating(0);
    setReviewText('');
    onClose();
  };

  const handleRatingClick = (value) => {
    setRating(value);
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-content bg-white">
        <button className="btn-close position-absolute top-0 end-0 m-3" onClick={onClose}></button>
        <div className="text-center">
          <p className="fw-semibold">Add Review</p>
          <img src={bookPNG || ""} alt="Book" className="img-modal" />
          <h5 className="fw-bold">{bookTitle || 'Book Title'}</h5>

          {/* <div className="d-flex justify-content-center mb-2 mt-3">
            {[1, 2, 3, 4, 5].map((star) => (
              <span key={star} onClick={() => handleRatingClick(star)} style={{ cursor: 'pointer' }}>
                {rating >= star ? <SVGStarFill /> : <SVGStar />}
              </span>
            ))}
          </div> */}

          <p className="text-center text-secondary small">Tap to Rate</p>

          <textarea
            className="form-control comment-area"
            rows="8"
            placeholder="Type your review..."
            value={reviewText}
            onChange={(e) => setReviewText(e.target.value)}
          />

          {/* Buttons */}
          <div className="d-flex flex-column align-items-center w-100">
            <button
                className="btn gray-btn mb-3 col-12"
                onClick={handleSubmit}
                disabled={reviewText.trim() === ''}
            >
                Submit Review
            </button>
            <button className="btn gray-btn col-12" onClick={onClose}>
                Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReviewModal;