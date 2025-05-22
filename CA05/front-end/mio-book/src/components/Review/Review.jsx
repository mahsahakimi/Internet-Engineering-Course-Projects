import React from 'react';
import StarRatingRenderer from '../StarRatingRenderer';


function Review({br}) {

    const formatted = new Date(`${br?.date?.year}-${br?.date?.month}-${br?.date?.day}`).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });

    return (
        <div className="review-card">
            <div className="card-body">
                <div className="row">
                    <div className="col-sm-1 col-2">
                        <div className="avatar d-flex align-items-center justify-content-center">
                            <h3 className="fw-bold">{br?.username?.charAt(0)}</h3>
                        </div>
                    </div>
                    <div className="col-sm-8 col-6">
                        <p className="fw-bold mb-1">{br?.username}</p>
                        <p className="text-secondary">{br?.comment}</p>
                    </div>
                    <div className="col-sm-3 col-4 text-end">
                        <StarRatingRenderer rating={br?.rate}/>
                        <small className="text-secondary">{formatted}</small>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Review;