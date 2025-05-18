import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import Footer from '../../components/Footer';
import BookCard from '../../components/BookCard';
import Nav from '../../components/Nav';
import '../../stylesheets/General.css';


function SearchResult() {
    const location = useLocation();
    const filterType = location.state?.type || '';
    const searchTerm = location.state?.term || '';
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (filterType && searchTerm) {
            quickSerach();
        }
    }, [filterType, searchTerm]);

    const quickSerach = async () => {

        setLoading(true);
        const endpoint =
            filterType === "Author"
                ? `/api/books/search_by_author?author=${encodeURIComponent(searchTerm)}`
                : `/api/books/search_by_title?title=${encodeURIComponent(searchTerm)}`;

        try {
            const result = await fetch(endpoint);
            const responseData = await result.json();
            setResults(Array.isArray(responseData?.data) ? responseData.data : []);
            
        } catch (error) {
            console.error('Search failed:', error);
            setResults([]);
            
        } finally {
            setLoading(false);
        }
    };


    return (
        <div>
            <Nav />
            <section className="container mb-5 mt-4">
                <h4 className="mb-4">Results for <strong>{filterType}: "{searchTerm}"</strong></h4>

                {loading ? (
                    <p>Loading...</p>
                ) : results.length === 0 ? (
                    <p>No results found.</p>
                ) : (
                    <div className="row row-cols-1 row-cols-sm-5  g-4">
                        {results.map((book, index) => (
                            <div className="col" key={index}>
                                <BookCard book = {book} />
                            </div>
                        ))}
                    </div>
                )}
            </section>
            <Footer />
        </div>
    );
};

export default SearchResult;