import React from 'react';
<<<<<<< HEAD
import '../../stylesheets/General.css';
import '../../stylesheets/Book.css';

function Review({ review }) {
  if (!review) {
    return null;
  }

  const { rate, comment, username } = review;
  const firstTwoLetters = username ? username.substring(0, 2).toUpperCase() : 'XX'; // For the avatar

  const renderStars = (rating) => {
    const stars = [];
    for (let i = 0; i < 5; i++) {
      if (i < rating) {
        stars.push(<i key={i} className="fas fa-star fa-sm text-warning"></i>);
      } else {
        stars.push(<i key={i} className="far fa-star fa-sm text-warning"></i>);
      }
    }
    return stars;
  };

  const formatDate = () => {
    const now = new Date();
    const month = now.toLocaleString('default', { month: 'long' });
    const day = now.getDate();
    const year = now.getFullYear();
    return `${month} ${day}, ${year}`;
  };

  return (
    <div className="review-card">
      <div className="card-body">
        <div className="row">
          <div className="col-sm-1 col-2">
            <div className="avatar d-flex align-items-center justify-content-center bg-secondary text-white rounded-circle">
              <span className="fw-bold">{firstTwoLetters}</span>
{/* ======= */}
{/* import StarRatingRenderer from '../StarRatingRenderer'; */}


{/* function Review({br}) { */}

{/*     const formatted = new Date(`${br?.date?.year}-${br?.date?.month}-${br?.date?.day}`).toLocaleDateString('en-US', { */}
{/*         year: 'numeric', */}
{/*         month: 'long', */}
{/*         day: 'numeric' */}
{/*     }); */}

{/*     return ( */}
{/*         <div className="review-card"> */}
{/*             <div className="card-body"> */}
{/*                 <div className="row"> */}
{/*                     <div className="col-sm-1 col-2"> */}
{/*                         <div className="avatar d-flex align-items-center justify-content-center"> */}
{/*                             <h3 className="fw-bold">{br?.username?.charAt(0)}</h3> */}
{/*                         </div> */}
{/*                     </div> */}
{/*                     <div className="col-sm-8 col-6"> */}
{/*                         <p className="fw-bold mb-1">{br?.username}</p> */}
{/*                         <p className="text-secondary">{br?.comment}</p> */}
{/*                     </div> */}
{/*                     <div className="col-sm-3 col-4 text-end"> */}
{/*                         <StarRatingRenderer rating={br?.rate}/> */}
{/*                         <small className="text-secondary">{formatted}</small> */}
{/*                     </div> */}
{/*                 </div> */}
{/* >>>>>>> refs/remotes/origin/main */}
            </div>
          </div>
          <div className="col-sm-8 col-6">
            <p className="fw-bold mb-1">{username || 'User'}</p>
            <p className="text-secondary">{comment || 'No comment provided.'}</p>
          </div>
          <div className="col-sm-3 col-4 text-end">
            <div className="text-warning mb-1">{renderStars(rate)}</div>
            <small className="text-secondary">{formatDate()}</small>
          </div>
        </div>
// <<<<<<< HEAD
//       </div>
//     </div>
//   );
// }
// =======
    );
};
{/* >>>>>>> refs/remotes/origin/main */}

export default Review;