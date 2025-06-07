import { useContext, useState } from 'react';
import {useNavigate } from 'react-router-dom';
import '../../stylesheets/Filter.css';
import React from 'react';
import { UserContext } from '../../UserContex';


function FilterSidebar({ show, onClose, onResults, genres}) {
    const { user, setUser } = useContext(UserContext);
    if (!show) return null;
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: '',
        author: '',
        genre: '',
        year: '',
        sort: 'review', 
        order: 'descending', 
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSortClick = (value) => {
        setFormData(prev => ({ ...prev, sort: value }));
    };

    const handleOrderClick = (value) => {
        setFormData(prev => ({ ...prev, order: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const { name, author, genre, year, sort, order } = formData;

        const fromYear = year.trim() === '' ? null : parseInt(year, 10);
        const toYear = year.trim() === '' ? null : parseInt(year, 10);


        const params = new URLSearchParams({
            title: name,
            author: author,
            genre : genre,
            fromYear: fromYear,
            toYear: toYear,
            sortBy: sort === 'review' ? 'reviewCount' : 'averageRating',
            sortOrder: order === 'ascending' ? 'asc' : 'desc',
        });

        const keys = Array.from(params.keys());

        for (const key of keys) {
            const value = params.get(key);
            if (!value || value === "" || value === "null") {
                params.delete(key);
            }
        }

        try {
            const response = await fetch(`/api/books/searchBooks?${params.toString()}`, {
                method: 'GET',
                headers: {
                    'Session-ID': user.userInfo.sessionId
                },
            });
            const responseData = await response.json();
            if (responseData.status === 'success') {

                onResults(responseData.data, params);
                onClose();
            }
            else{
                navigate('/error', { state: { message: responseData.message}});
            }

        } catch (error) {
            console.error('Fetch error:', err);
            navigate('/error', { state: { message: 'An error occurred while searching for books.' } });
        }
    };

    return (
        <div className="sidebar-overlay" onClick={onClose}>
        <div className="sidebar-container" onClick={(e) => e.stopPropagation()}>
            <div className="sidebar-header d-flex justify-content-between">
            <div className="flex-grow-1 text-center">
                <h4 className="modal-title">Filters</h4>
            </div>
            <button
                type="button"
                className="btn-close"
                aria-label="Close"
                style={{ transform: 'scale(0.8)' }}
                onClick={onClose}
            />
            </div>
            <hr />
            <form className="filter-form" onSubmit={handleSubmit}>
            <div className="row mb-4">
                <div className="col-4 d-flex align-items-center">
                <label htmlFor="name">Book Name:</label>
                </div>
                <div className="col-8 smaller-vertical">
                <input
                    id="name"
                    type="text"
                    name="name"
                    className="form-control"
                    value={formData.name}
                    onChange={handleChange}
                />
                </div>
            </div>

            <div className="row mb-4">
                <div className="col-4 d-flex align-items-center">
                <label htmlFor="author">Author Name:</label>
                </div>
                <div className="col-8 smaller-vertical">
                <input
                    id="author"
                    type="text"
                    name="author"
                    className="form-control"
                    value={formData.author}
                    onChange={handleChange}
                />
                </div>
            </div>

            <div className="row mb-4">
                <div className="col-4 d-flex align-items-center">
                <label htmlFor="genre">Genre:</label>
                </div>
                <div className="col-8">
                    <select
                        id="genre"
                        name="genre"
                        className="form-select"
                        value={formData.genre}
                        onChange={handleChange}
                    >
                        <option value="">Select genre</option>
                        {genres.map((genre) => (
                        <option key={genre} value={genre}>
                            {genre}
                        </option>
                        ))}
                    </select>
                </div>
            </div>

            <div className="row mb-4">
                <div className="col-4 d-flex align-items-center">
                <label htmlFor="year">Published Year:</label>
                </div>
                <div className="col-8 smaller-vertical">
                <input
                    id="year"
                    type="text"
                    name="year"
                    className="form-control"
                    value={formData.year}
                    onChange={handleChange}
                />
                </div>
            </div>

            <div className="row mb-4">
                <div className="col-4 d-flex align-items-center">
                <label>Sort By:</label>
                </div>
                <div className="col-8 d-flex gap-2 justify-content-between smaller-vertical">
                <button
                    type="button"
                    className={`btn flex-fill ${
                    formData.sort === 'rating' ? 'green-btn' : 'gray-btn'
                    }`}
                    onClick={() => handleSortClick('rating')}
                >
                    Rating
                </button>
                <button
                    type="button"
                    className={`btn flex-fill ${
                    formData.sort === 'review' ? 'green-btn' : 'gray-btn'
                    }`}
                    onClick={() => handleSortClick('review')}
                >
                    Reviews
                </button>
                </div>
            </div>

            <div className="row mb-4">
                <div className="col-4 d-flex align-items-center">
                <label>Order:</label>
                </div>
                <div className="col-8 d-flex gap-2 justify-content-between smaller-vertical">
                <button
                    type="button"
                    className={`btn flex-fill ${
                    formData.order === 'descending' ? 'green-btn' : 'gray-btn'
                    }`}
                    onClick={() => handleOrderClick('descending')}
                >
                    Descending
                </button>
                <button
                    type="button"
                    className={`btn flex-fill ${
                    formData.order === 'ascending' ? 'green-btn' : 'gray-btn'
                    }`}
                    onClick={() => handleOrderClick('ascending')}
                >
                    Ascending
                </button>
                </div>
            </div>

            <button type="submit" className="btn green-btn col-12">
                Apply
            </button>
            </form>
        </div>
        </div>
    );
}

export default FilterSidebar;