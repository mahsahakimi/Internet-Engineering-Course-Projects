import React from 'react';
import '../../stylesheets/General.css';

function BookContentText({ content }) {
  return (
    <div className='container col-sm-10 bb white-box'>
      <p>
        {content || 'Book content will be displayed here.'}
      </p>
    </div>
  );
}

export default BookContentText;