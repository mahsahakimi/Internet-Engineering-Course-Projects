import React, { useContext, useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import Footer from '../../components/Footer';
import BookCard from '../../components/BookCard';
import Nav from '../../components/Nav';
import '../../stylesheets/General.css';
import FilterSidebar from '../../components/FilterSidebar/FilterSidebar';
import { UserContext } from '../../UserContex';


const PAGE_SIZE = 10;

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
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [mode, setMode] = useState('all');

    const applyPaginatedData = (data) => {
        setResults(Array.isArray(data?.books) ? data.books : []);
        setTotalPages(data?.totalPages || 1);
        setCurrentPage(data?.currentPage || 1);
    };

    const handleSearchResults = (data, param) => {
        applyPaginatedData(data);
        setFilters(param);
        setMode('filter');
    };


    useEffect(() => {
        if (!user) return;
        if (filterType && searchTerm) {
            setMode('quick');
            quickSerach(1);
        } else {
            setMode('all');
            fetchAllBooks(1);
        }
        fetchGenres();
    }, [filterType, searchTerm, location.key, user]);

    const fetchAllBooks = async (page = 1) => {
        setLoading(true);
        setFilters(new URLSearchParams());
        try {
            const result = await fetch(`/api/books/all?page=${page}&pageSize=${PAGE_SIZE}`, {
                headers: {
                    'Authorization': `Bearer ${user.userInfo.token}`,
                },
            });
            const responseData = await result.json();
            applyPaginatedData(responseData?.data);
        } catch (error) {
            console.error('Failed to fetch all books:', error);
            setResults([]);
            setTotalPages(1);
        } finally {
            setLoading(false);
        }
    };


    const quickSerach = async (page = 1) => {

        setLoading(true);
        const endpoint =
            filterType === "Author"
                ? `/api/books/searchBooks?author=${encodeURIComponent(searchTerm)}&page=${page}&pageSize=${PAGE_SIZE}`
                : `/api/books/searchBooks?title=${encodeURIComponent(searchTerm)}&page=${page}&pageSize=${PAGE_SIZE}`;

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
            applyPaginatedData(responseData?.data);
            
        } catch (error) {
            console.error('Search failed:', error);
            setResults([]);
            setTotalPages(1);

        } finally {
            setLoading(false);
        }
    };

    const fetchFilteredPage = async (page) => {
        setLoading(true);
        try {
            const requestParams = new URLSearchParams(filters);
            requestParams.set('page', page);
            requestParams.set('pageSize', PAGE_SIZE);
            const result = await fetch(`/api/books/searchBooks?${requestParams.toString()}`, {
                headers: {
                    'Authorization': `Bearer ${user.userInfo.token}`,
                },
            });
            const responseData = await result.json();
            applyPaginatedData(responseData?.data);
        } catch (error) {
            console.error('Filtered search failed:', error);
            setResults([]);
            setTotalPages(1);
        } finally {
            setLoading(false);
        }
    };

    const handlePageChange = (newPage) => {
        if (newPage < 1 || newPage > totalPages || newPage === currentPage) return;
        if (mode === 'all') {
            fetchAllBooks(newPage);
        } else if (mode === 'quick') {
            quickSerach(newPage);
        } else {
            fetchFilteredPage(newPage);
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
                        <>
                            <div className="row row-cols-1 row-cols-sm-5  g-4">
                                {results.map((book, index) => (
                                    <div className="col" key={index}>
                                        <BookCard book = {book} />
                                    </div>
                                ))}
                            </div>
                            {totalPages > 1 && (
                                <nav aria-label="Results pagination" className="d-flex justify-content-center mt-4">
                                    <ul className="pagination mb-0">
                                        <li className={`page-item ${currentPage <= 1 ? 'disabled' : ''}`}>
                                            <button
                                                type="button"
                                                className="page-link"
                                                onClick={() => handlePageChange(currentPage - 1)}
                                                disabled={currentPage <= 1}
                                            >
                                                &laquo;
                                            </button>
                                        </li>
                                        {Array.from({ length: totalPages }, (_, i) => i + 1).map((pageNum) => (
                                            <li key={pageNum} className={`page-item ${currentPage === pageNum ? 'active' : ''}`}>
                                                <button
                                                    type="button"
                                                    className="page-link"
                                                    onClick={() => handlePageChange(pageNum)}
                                                >
                                                    {pageNum}
                                                </button>
                                            </li>
                                        ))}
                                        <li className={`page-item ${currentPage >= totalPages ? 'disabled' : ''}`}>
                                            <button
                                                type="button"
                                                className="page-link"
                                                onClick={() => handlePageChange(currentPage + 1)}
                                                disabled={currentPage >= totalPages}
                                            >
                                                &raquo;
                                            </button>
                                        </li>
                                    </ul>
                                </nav>
                            )}
                        </>
                    )}
                </div>
            </section>
            <Footer />
        </div>
    );
};

export default SearchResult;