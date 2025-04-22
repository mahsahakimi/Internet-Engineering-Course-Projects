import React from 'react';

function Review() {
  return (
    <div className="card">
        <div className="card-body">
            <div className="row">
                <div className="col-sm-1 col-2">
                    <div className="avatar d-flex align-items-center justify-content-center">
                        <span className="text-secondary fw-bold">TW</span>
                    </div>
                </div>
                <div className="col-sm-8 col-6">
                    <p className="fw-bold mb-1">The Person</p>
                    <p className="text-secondary">I bought it 3 weeks ago and now come back just to say "Awesome". I really enjoyed it.</p>
                </div>
                <div className="col-sm-3 col-4 text-end">
                    <div className="text-warning mb-1">
                        <i className="fas fa-star"></i>
                        <i className="fas fa-star"></i>
                        <i className="fas fa-star"></i>
                        <i className="fas fa-star"></i>
                        <i className="far fa-star"></i>
                    </div>
                    <small className="text-secondary">February 20, 2025</small>
                </div>
            </div>
        </div>
    </div>
  );
};

export default Review;