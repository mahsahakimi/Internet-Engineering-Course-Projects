import React from 'react';
import Author_img from '../../images/author.jpg';
import Header_img from '../../images/author-header.jpg';
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';

function AuthorHeader({ name }) {
  return (
    <div className="position-relative">
      <img src={Header_img} className="col-sm-12 header-bg" alt="Header Background" />

      <div className="container position-absolute">
        <div className="row">
          <div className="col-sm-1"></div>
          <div className="col-sm-3">
            <img src={Author_img} className="author-img" alt={name} />
          </div>
        </div>
      </div>
    </div>
  );
}

export default AuthorHeader;