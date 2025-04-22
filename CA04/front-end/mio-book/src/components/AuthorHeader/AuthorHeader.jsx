import React from 'react';
import Author_img from '../../images/author.jpg'
import Header_img from '../../images/author-header.jpg'
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';

function AuthorHeader() {
  return (
    <div class="position-relative">
        <img src={Header_img} class="col-sm-12 header-bg" alt="Header Background"/>

        <div class="container position-absolute">
        <div class="row">
            <div class="col-sm-1"></div>
            <div class="col-sm-3">
            <img src={Author_img} class="author-img" alt="Author Image"/>
            </div>
        </div>
        </div>
    </div>
  );
}

export default AuthorHeader;