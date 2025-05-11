import React, { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../../UserContex';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import CartBookDetailsCard from '../../components/CartBookDetailsCard';
import '../../stylesheets/General.css';
import EmptySpace from '../../components/EmptySpace';
import SVGCartIsEmpty from '../../components/SVGCartIsEmpty';

function BuyCartPage() {
    const { user, setUser } = useContext(UserContext);
    const [cart, setCart] = useState([]);
    const navigate = useNavigate();

    if(!user){
        const localUser = JSON.parse(localStorage.getItem('activeUser'));
        setUser(localUser)
    }

    useEffect(() => {
        if (user?.userInfo?.role === "admin") {
            navigate("/admin", { replace: true });
        }
    }, [user, navigate]);

    const fetchCart = async () => {
        try {
            const response = await fetch('/api/carts/details');
            const responseData = await response.json();

            if (responseData.status === "success") {
                setCart(responseData.data);
            } else {
                console.error("Failed to fetch Cart details.");
                navigate('/error', { state: { message: responseData.message || "Failed to fetch Cart details." } });
            }
        } catch (error) {
            console.error("Error fetching Cart details:", error);
            navigate('/error', { state: { message: "Error fetching Cart details." } });
        }
    };

    useEffect(() => {
        fetchCart();
    }, []);

    const handleRemoveItem = async () => {
        fetchCart();
    };

    const handlePurchase = async () => {
        try {
            const response = await fetch('/api/carts/purchase', {
                method: 'POST',
                // credentials: 'include', // Important if your backend uses cookies/session
            });
            const responseData = await response.json();

            if (responseData.status === 'success') {
                fetchCart();
                // show success message
            } else {
                console.error('Purchase failed.');
                navigate('/error', { state: { message: responseData.message || 'Purchase failed.' } });
            }
        } catch (error) {
            console.error('Error during purchase:', error);
            navigate('/error', { state: { message: 'Error occurred during purchase.' } });
        }
    };

    return (
        <>
            <Nav />
            <EmptySpace />
            <div className="row justify-content-center">
                <div className="container white-box col-sm-10">
                    <div className="d-flex justify-content-between align-items-center mb-4">
                        <h4 className="fw-bold mb-0 ms-1">
                            <i className="bi bi-cart3"></i> Cart
                        </h4>
                        <h6 className="mb-0 me-5">Total Price : ${cart?.totalCost}</h6>
                    </div>

                    {cart?.items?.length > 0 ? (
                    <table className="table">
                        <thead className="table-light">
                            <tr>
                                <th>Image</th>
                                <th>Name</th>
                                <th>Author</th>
                                <th>Price</th>
                                <th>Borrow Days</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {cart.items.map((item, index) => (
                                <CartBookDetailsCard
                                    cartElement={item}
                                    onRemove={handleRemoveItem}
                                    key={index}
                                />
                            ))}
                        </tbody>
                    </table>
                ) : (
                    <div className="empty-state">
                        <SVGCartIsEmpty />
                        <p className='text-secondary'>No book selected!</p>
                    </div>
                )}

                    
                    <div className="d-flex justify-content-center">
                        <button 
                            className={`btn col-sm-4 ${cart?.items?.length === 0 ? 'gray-btn' : 'green-btn'}`}
                            onClick={handlePurchase}
                            disabled={cart?.items?.length == 0}
                        >Purchase</button>

                    </div>
                </div>
            </div>

            <Footer />
        </>
    );
};

export default BuyCartPage;