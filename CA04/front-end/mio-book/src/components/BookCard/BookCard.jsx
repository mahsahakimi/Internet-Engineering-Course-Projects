import React from 'react';
import '../../stylesheets/General.css';
import bookpng from '../../../src/images/book.png';
// <<<<<<< HEAD
// // fetch pic for each
//
// function BookCard({ book }) {
//   if (!book) {
//     return(
//         <div className="book-card loading-placeholder">
//             Loading book...
//         </div>
//     );
//   }
//
//   return (
//     <div className="book-card">
//       <a href={`/book/content?title=${encodeURIComponent(book.title)}`}>
//         <img src={bookpng} className="book-img book-cover col-12" alt={book.title} />
//       </a>
//       <div className="p-3 text-center">
//         <h5 className="mb-1">{book.title || 'Book Title'}</h5>
//         <p className="text-muted mb-2">{book.author || 'Author Name'}</p>
//       </div>
//       <div className="row">
//         <div className="star-rating mb-2 col-7">
//           {/* You would dynamically render stars based on book.averageRating */}
//           <i className="fas fa-star fa-xs"></i>
//           <i className="fas fa-star fa-xs"></i>
//           <i className="fas fa-star fa-xs"></i>
//           <i className="fas fa-star fa-xs"></i>
//           <i className="far fa-star fa-xs"></i>
//         </div>
//         <p className="col-5">${book.price ? (book.price / 100).toFixed(2) : '0.00'}</p>
//       </div>
//       <div className="row justify-content-center">
//         <button className="green-btn col-10 mb-2">Add to Cart</button>
//       </div>
//     </div>
//   );
// };
//
// export default BookCard;
//
//
//
//
// =======
import { Link } from 'react-router-dom';
import StarRatingRenderer from '../StarRatingRenderer';


function BookCard({book}) {

    const rating = book?.averageRating || 0;

    return (
        <div className="book-card">
            <Link to={`/book-details?title=${encodeURIComponent(book?.title)}`}>
                <img src={bookpng} className="book-img book-cover col-12" alt={book.title} />
            </Link>            
            <div className="p-3 text-center">
                <h5 className="mb-1">{book?.title}</h5>
                <p className="text-muted mb-2">{book?.author}</p>
            </div>
            <div className="row align-items-center mb-3 px-3">
                <div className="col-8">
                    {<StarRatingRenderer rating={rating}/>}
                </div>
                <div className="col-4">
                    <span className="fs-6">${book?.price}</span>
                </div>
            </div>
            <div className="row justify-content-center">
                <button className="green-btn col-10 mb-2 ">Add to Cart</button>
            </div>
        </div>
    );
};

export default BookCard;
>>>>>>> refs/remotes/origin/main
