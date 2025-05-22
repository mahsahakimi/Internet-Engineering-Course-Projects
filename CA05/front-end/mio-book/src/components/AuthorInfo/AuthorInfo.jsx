import React from 'react';
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';

function AuthorInfo({ author, bookCount }) {
  const { name, penName, nationality, born } = author;
  const died = author.died ? new Date(author.died).toLocaleDateString() : 'Still Alive';

  return (
    <div className="container mb-5">
      <div className="row justify-content-center">
        <div className="col-3">
          <h2 className="fw-bold">{name || 'Author Name'}</h2>
        </div>
        <div className="col-6 col-sm-2 mb-2">
          <small>Pen Name</small>
          <br />
          <strong className="text-dark">{penName || '-'}</strong>
        </div>
        <div className="col-6 col-sm-2 mb-2">
          <small>Nationality</small>
          <br />
          <strong className="text-dark">{nationality || '-'}</strong>
        </div>
        <div className="col-6 col-sm-2 mb-2">
          <small>Born</small>
          <br />
          <strong className="text-dark">{born || '-'}</strong>
        </div>
        <div className="col-6 col-sm-2 mb-2">
          <small>Died</small>
          <br />
          <strong className="text-dark">{died}</strong>
        </div>
        <div className="col-6 col-sm-1 mb-2">
          <small>Books</small>
          <br />
          <strong className="text-dark">{bookCount}</strong>
        </div>
      </div>
    </div>
  );
}

export default AuthorInfo;