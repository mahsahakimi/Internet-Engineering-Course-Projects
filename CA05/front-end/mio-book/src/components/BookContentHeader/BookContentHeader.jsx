import React from 'react';
import '../../stylesheets/General.css';

function BookContentHeader({ title, author }) {
  return (
    <div className='container col-sm-10 bb white-box'>
      <div className='row'>
        <div className='col-sm-6'>
          <h4 className="fw-bold">
            <i class="bi bi-journal-text"></i>
            {title || 'Book Title'}
          </h4>
        </div>
        <div className='col-6 text-end'>
          <p>by {author || 'Author Name'}</p>
        </div>
      </div>
    </div>
  );
}

export default BookContentHeader;