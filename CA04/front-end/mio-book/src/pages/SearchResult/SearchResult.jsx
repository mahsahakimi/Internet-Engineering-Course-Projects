import React from 'react';
import Footer from '../../components/Footer';
import BookCard from '../../components/BookCard';
import Nav from '../../components/Nav';
import '../../stylesheets/General.css';



function SearchResult() {
  return (
    <body>
        <Nav />
        <section class="container mb-5">
            <h4 class="mb-4">Results for  [serach parameter]  </h4>
            <div class="row row-cols-1 row-cols-sm-5 mb-5">
                <div class="col">
                    <BookCard />
                </div>

                <div class="col">
                    <BookCard />
                </div>

                <div class="col">
                    <BookCard />
                </div>

                <div class="col">
                    <BookCard />
                </div>

                <div class="col">
                    <BookCard />
                </div>
            </div>

            <div class="row row-cols-1 row-cols-sm-5 ">
                <div class="col">
                    <BookCard />
                </div>

                <div class="col">
                    <BookCard />
                </div>

                <div class="col">
                    <BookCard />
                </div>

                <div class="col">
                    <BookCard />
                </div>

                <div class="col">
                    <BookCard />
                </div>
            </div>
        </section>

        <div class="pg-container">
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <li class="page-item"><a class="page-link" href="#" aria-label="Previous">&lsaquo;</a></li>
                    <li class="page-item active"><a class="page-link" href="#">1</a></li>
                    <li class="page-item"><a class="page-link" href="#">2</a></li>
                    <li class="page-item"><a class="page-link" href="#">3</a></li>
                    <li class="page-item"><a class="page-link" href="#">4</a></li>
                    <li class="page-item"><a class="page-link" href="#">5</a></li>
                    <li class="page-item"><a class="page-link" href="#" aria-label="Next">&rsaquo;</a>
                    </li>
                </ul>
            </nav>
        </div>

        <Footer />
    </body>
  );
};

export default SearchResult;