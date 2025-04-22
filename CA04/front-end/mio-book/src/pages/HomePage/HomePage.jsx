import React from 'react';
import styles from './HomePage.module.css';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';

function HomePage() {
    return (
        <>
          <Nav />
          <h1> HOME PAGE </h1>
          <Footer />
        </>
    );
};

export default HomePage;