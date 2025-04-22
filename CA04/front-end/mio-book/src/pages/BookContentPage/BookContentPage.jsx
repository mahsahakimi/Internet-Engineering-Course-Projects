import React from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import EmptySpace from '../../components/EmptySpace';
import BookContentHeader from '../../components/BookContentHeader';
import '../../stylesheets/General.css';
import BookContentText from '../../components/BookContentText';


function BookContentPage() {
  return (
    <body>
        <Nav />
        
        <EmptySpace/>
        
        <BookContentHeader/>
        
        <EmptySpace/>

        <BookContentText/>
        
        <Footer />
        
    </body>
  );
};

export default BookContentPage;