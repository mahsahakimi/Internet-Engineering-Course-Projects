import React from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import Author_img from '../../images/author.jpg'
import Header_img from '../../images/author-header.jpg'
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';
import BookCard from '../../components/BookCard';


function AuthorPage() {
  return (
    <body>
        <Nav />
        
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


        <section className="container mb-5">
            <div className="row row-cols-1 row-cols-sm-5 ">
                <div className="col">
                    <BookCard />
                </div>

                <div className="col">
                    <BookCard />
                </div>

                <div className="col">
                    <BookCard />
                </div>

                <div className="col">
                    <BookCard />
                </div>

                <div className="col">
                    <BookCard />
                </div>
            </div>
        </section>

        <section className="container mb-5">
            <div className="row row-cols-1 row-cols-sm-5 ">
                <div className="col">
                    <BookCard />
                </div>

                <div className="col">
                    <BookCard />
                </div>

                <div className="col">
                    <BookCard />
                </div>

                <div className="col">
                    <BookCard />
                </div>

                <div className="col">
                    <BookCard />
                </div>
            </div>
        </section>

        <Footer />
    </body>
  );
};

export default AuthorPage;