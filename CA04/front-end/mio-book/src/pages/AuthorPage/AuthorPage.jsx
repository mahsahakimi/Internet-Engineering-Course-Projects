import React from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import BookCard from '../../components/BookCard';
import AuthorInfo from '../../components/AuthorInfo';
import AuthorHeader from '../../components/AuthorHeader';
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';


function AuthorPage() {
  return (
    <body>
        <Nav />
        
        <AuthorHeader/>

        <AuthorInfo/>


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