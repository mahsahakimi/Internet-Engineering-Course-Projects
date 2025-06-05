import React, { useState , useEffect, useContext} from 'react';
import { useNavigate } from 'react-router-dom';
import '../../stylesheets/Modal.css';
import { UserContext } from '../../UserContex';

const ModalAddToCart = ({ show, onClose, bookTitle, bookPrice, updateBookStatus}) => {
    const { user, setUser } = useContext(UserContext);
    const [borrowChecked, setBorrowChecked] = useState(false);
    const [selectedDay, setSelectedDay] = useState(null);
    const navigate = useNavigate();

    if (!show) return null;

    const calculatePrice = () => {
        if (!borrowChecked) return bookPrice?.toFixed(2);
        return (bookPrice * selectedDay / 10).toFixed(2);
    };

    const handleAdd = async () => {
        const endpoint = '/api/carts/add';
        const payload = borrowChecked ? { title:bookTitle, days:selectedDay } : { title: bookTitle, days: 0 };

        try {
            const res = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Session-ID' : user.userInfo.sessionId,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload)
            });

            const result = await res.json();
            if (result.status === 'success') {
                updateBookStatus('cart');
                setBorrowChecked(false);
                setSelectedDay(null);
                onClose();
            } else {
                console.error(result.message || 'Failed to add to cart.');
                navigate('/error', { state: { message: result.message || 'Failed to add to cart.' } });
            }
        } catch (err) {
            console.error('Add to Cart error:', err);
            navigate('/error', { state: { message: err.message } });
        }
    };

    return (
        <div className="modal d-block" tabIndex="-1" role="dialog" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
        <div className="modal-dialog modal-dialog-centered" role="document">
            <div className="modal-content p-3">
            <div className="modal-header d-flex justify-content-between">
                <div className="flex-grow-1 text-center">
                    <h6 className="modal-title mb-0">Add to Cart</h6>
                </div>
                <button type="button" className="btn-close" aria-label="Close" style={{ transform: 'scale(0.6)' }} onClick={onClose}></button>
            </div>

            <div className="modal-body">
                <p>Would you like to buy or borrow this book?</p>

                <div className="form-check mb-3">
                <input
                    className="form-check-input"
                    type="checkbox"
                    id="borrowCheckbox"
                    checked={borrowChecked}
                    onChange={(e) => {
                    setBorrowChecked(e.target.checked);
                    setSelectedDay(null);
                    }}
                />
                <label className="form-check-label" htmlFor="borrowCheckbox">
                    Borrow this book
                </label>
                </div>

                {borrowChecked && (
                <div className="row g-2 mb-3">
                    {[...Array(9)].map((_, i) => (
                    <div className="col-4" key={i + 1}>
                        <button
                        type="button"
                        className={`btn col-12 ${selectedDay === i + 1 ? 'green-btn' : 'gray-btn'}`}
                        onClick={() => setSelectedDay(i + 1)}
                        >
                        {i + 1} {i + 1 === 1 ? 'Day' : 'Days'}
                        </button>
                    </div>
                    ))}
                </div>
                )}
            </div>

            <div className="modal-footer d-flex justify-content-between">
                <p className="mb-0 col-6">
                    Final Price: <strong>${calculatePrice()}</strong>
                </p>
                <button
                    type="button"
                    className="btn green-btn col-5"
                    onClick={handleAdd}
                    disabled={borrowChecked && !selectedDay}
                >
                    Add
                </button>
                </div>

            </div>
        </div>
        </div>
    );
};

export default ModalAddToCart;