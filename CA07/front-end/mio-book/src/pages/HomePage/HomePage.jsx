import React, { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Footer from '../../components/Footer';
import BookCard from '../../components/BookCard';
import Nav from '../../components/Nav';
import book from '../../../src/images/book.png';
import '../../stylesheets/HomePage.css';
import '../../stylesheets/General.css';
import EmptySpace from '../../components/EmptySpace';
import { UserContext } from '../../UserContex';


function HomePage() {
    const { user, setUser } = useContext(UserContext);
    const [newReleases, setNewReleases] = useState([]);
    const [topRated, setTopRated] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        if (!user) return;
        fetchNewReleases();
        fetchTopRated();
    }, [user]);

    const fetchNewReleases = async () => {
        try {
            const response = await fetch('/api/books/newRelease', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${user.userInfo.token}`
                }
            });
            const responseData = await response.json();

            if (responseData.status === "success") {
                setNewReleases(responseData.data);
            } else {
                console.error("Failed to fetch new releases.");
                navigate('/error', { state: { message: responseData.message || "Failed to fetch new releases." } });
            }
        } catch (error) {
            console.error("Error fetching new releases:", error);
            navigate('/error', { state: { message: "Error fetching new releases." } });
        }
    };

    const fetchTopRated = async () => {
        try {
            const response = await fetch('/api/books/topRated', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${user.userInfo.token}`
                }
            });
            const responseData = await response.json();

            if (responseData.status === "success") {
                setTopRated(responseData.data);
            } else {
                console.error("Failed to fetch top rated.");
                navigate('/error', { state: { message: responseData.message || "Failed to fetch top rated." } });
            }
        } catch (error) {
            console.error("Error fetching top rated:", error);
            navigate('/error', { state: { message: "Error fetching top rated." } });
        }
    };



    return (
        <>
            <Nav />
            <EmptySpace />
            <section className="intro-section py-5 mb-5">
                <div className="container">
                    <div className="row">
                        <div className="col-sm-1"> </div>
                        <div className="col-sm-5">
                            <h2 className="mb-3">Welcome to MioBook – the online bookstore where you can buy or borrow books with ease.</h2>
                            <p>Whether you're looking for the latest bestseller, a classNameic novel, or a niche title, MioBook has you covered.</p>
                            <p>Here, you can quickly find books by title, author, and genre. And if you're not sure to buy? Try borrowing instead! Rent a book for just a fraction of the price and enjoy full access for a set period.</p>
                            <p>Your next great read is just a click away. Visit MioBook today and let the perfect book find you! 📚✨</p>
                            <button
                                type="button"
                                className="btn green-btn mt-2 px-4"
                                onClick={() => navigate('/results')}
                            >
                                Browse All Books →
                            </button>
                        </div>
                        <div className="col-sm-1"> </div>
                        <div className="col-sm-4">
                            <img src={book} alt="Bookshelf" className="main-home-image img-fluid" />
                        </div>
                        <div className="col-sm-1"> </div>
                    </div>
                </div>
            </section>

            <section className="container mb-5">
                <h2 className="mb-4">New Releases</h2>
                <div className="row row-cols-1 row-cols-sm-5">
                    {newReleases.length > 0 ? (
                        newReleases.map((book, index) => (
                            <div className="col" key={index}>
                                <BookCard book={book} />
                            </div>
                        ))
                    ) : (
                        <p>Loading new releases...</p>
                    )}
                </div>
            </section>


            <section className="container mb-5">
                <h2 className="mb-4">Top Rated</h2>
                <div className="row row-cols-1 row-cols-sm-5 ">
                {topRated.length > 0 ? (
                        topRated.map((book, index) => (
                            <div className="col" key={index}>
                                <BookCard book={book} />
                            </div>
                        ))
                    ) : (
                        <p>Loading top rated books...</p>
                    )}
                </div>
            </section>

            <Footer />
        </>
    );
};

export default HomePage;