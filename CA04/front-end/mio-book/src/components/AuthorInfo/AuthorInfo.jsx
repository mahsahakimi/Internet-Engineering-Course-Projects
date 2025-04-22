import React from 'react';
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';

function AuthorInfo() {
  return (
    <div class="container mb-5">
        <div class="row justify-content-center">
        <div class="col-4">
            <h2 class="fw-bold">Author Name</h2>
        </div>
        <div class="col-6 col-sm-1 mb-3">
            <small>Pen Name</small>
            <br/>
            <strong class="text-dark">Authorica</strong>
        </div>
        <div class="col-6 col-sm-1 mb-3">
            <small>Nationality</small>
            <br/>
            <strong class="text-dark">German</strong>
        </div>
        <div class="col-6 col-sm-2 mb-3">
            <small>Born</small>
            <br/>
            <strong class="text-dark">1998-02-01</strong>
        </div>
        <div class="col-6 col-sm-2 mb-3">
            <small>Died</small>
            <br/>
            <strong class="text-dark">2024-10-24</strong>
        </div>
        <div class="col-6 col-sm-1 mb-3">
            <small>Books</small>
            <br/>
            <strong class="text-dark">16</strong>
        </div>
        </div>
    </div>
  );
}

export default AuthorInfo;