import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import Footer from '../../components/Footer';
import BookCard from '../../components/BookCard';
import Nav from '../../components/Nav';
import '../../stylesheets/General.css';
import FilterSidebar from '../../components/FilterSidebar/FilterSidebar';


function SearchResult() {
    const location = useLocation();
    const filterType = location.state?.type || '';
    const searchTerm = location.state?.term || '';
    const [results, setResults] = useState([]);
    const [filters, setFilters] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showSidebar, setShowSidebar] = useState(false);

    const handleSearchResults = (data, param) => {
        setResults(data);
        setFilters(param)
    };


    useEffect(() => {
        if (filterType && searchTerm) {
            quickSerach();
        }
    }, [filterType, searchTerm]);

    const quickSerach = async () => {

        setLoading(true);
        const endpoint =
            filterType === "Author"
                ? `/api/books/searchBooks?author=${encodeURIComponent(searchTerm)}`
                : `/api/books/searchBooks?title=${encodeURIComponent(searchTerm)}`;

        try {
            setFilters (new URLSearchParams({
                title: filterType === 'Author'? null : searchTerm,
                author: filterType !== 'Author'? null : searchTerm,
                sortBy: 'reviewCount',
                sortOrder: 'desc',
            }))
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
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <h4 className="mb-0">
                        Results for [{filters.toString()}]
                    </h4>
                    <button onClick={() => setShowSidebar(true)} className="btn green-btn">
                        <i class="bi bi-filter"></i> Filter
                    </button>
                    <FilterSidebar show={showSidebar} onClose={() => setShowSidebar(false)} onResults={handleSearchResults}/>
                </div>

                
                <div>
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
                </div>
            </section>
            <Footer />
        </div>
    );
};

export default SearchResult;