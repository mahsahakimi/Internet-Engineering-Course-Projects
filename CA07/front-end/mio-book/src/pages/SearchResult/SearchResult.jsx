import React, { useContext, useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import Footer from '../../components/Footer';
import BookCard from '../../components/BookCard';
import Nav from '../../components/Nav';
import '../../stylesheets/General.css';
import FilterSidebar from '../../components/FilterSidebar/FilterSidebar';
import { UserContext } from '../../UserContex';


function SearchResult() {
    const { user, setUser } = useContext(UserContext);
    const location = useLocation();
    const filterType = location.state?.type || '';
    const searchTerm = location.state?.term || '';
    const [results, setResults] = useState([]);
    const [genres, setGenres] = useState([]);
    const [filters, setFilters] = useState(new URLSearchParams());
    const [loading, setLoading] = useState(true);
    const [showSidebar, setShowSidebar] = useState(false);

    const handleSearchResults = (data, param) => {
        setResults(data);
        setFilters(param)
    };


    useEffect(() => {
        if (!user) return;
        if (filterType && searchTerm) {
            quickSerach();
        } else {
            fetchAllBooks();
        }
        fetchGenres();
    }, [filterType, searchTerm, user]);

    const fetchAllBooks = async () => {
        setLoading(true);
        setFilters(new URLSearchParams());
        try {
            const result = await fetch('/api/books/all', {
                headers: {
                    'Authorization': `Bearer ${user.userInfo.token}`,
                },
            });
            const responseData = await result.json();
            setResults(Array.isArray(responseData?.data) ? responseData.data : []);
        } catch (error) {
            console.error('Failed to fetch all books:', error);
            setResults([]);
        } finally {
            setLoading(false);
        }
    };


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
            const result = await fetch(endpoint, {
                headers: {
                    'Authorization': `Bearer ${user.userInfo.token}`,
                },
            });
            const responseData = await result.json();
            setResults(Array.isArray(responseData?.data) ? responseData.data : []);
            
        } catch (error) {
            console.error('Search failed:', error);
            setResults([]);

        } finally {
            setLoading(false);
        }
    };

    const fetchGenres = async () => {
        try {
            const response = await fetch('/api/genres/getAll', {
                headers: {
                    'Authorization': `Bearer ${user.userInfo.token}`,
                },
            });
            const responseData = await response.json();

            if (responseData.status === "success") {
                setGenres(responseData.data);
            } else {
                console.error("Failed to fetch Genres");
            }
        } catch (error) {
            console.error("Error fetching books for admin:", error);
        }
    };


    return (
        <div>
            <Nav />
            <section className="container mb-5 mt-4">
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <h4 className="mb-0">
                        {filters.toString() ? `Results for [${filters.toString()}]` : 'All Books'}
                    </h4>
                    <button onClick={() => setShowSidebar(true)} className="btn green-btn">
                        <i class="bi bi-filter"></i> Filter
                    </button>
                    <FilterSidebar show={showSidebar} onClose={() => setShowSidebar(false)} onResults={handleSearchResults} genres={genres}/>
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