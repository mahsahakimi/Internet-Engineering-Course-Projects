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

    const fetchHistory = async () => {
        try {
            const response = await fetch('/api/carts/details');
            if (!response.ok) {
                console.error(`Failed to fetch cart details: ${response.status}`);
                const responseData = await response.json();
                navigate('/error', { state: { message: responseData.message } });
                return;
            }
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
        fetchHistory();
    }, []);


    return (
        <>
            <Nav />
            <EmptySpace />
            <div className="row justify-content-center">
                <div className="container white-box col-sm-10">
                    <div className="d-flex justify-content-between align-items-center mb-4">
                        <h4 className="fw-bold mb-0 ms-1">
                            <i class="bi bi-clock-history"></i> History
                        </h4>
                    </div>
                    <table className="table">
                        <thead className="table-light">
                        <tr>
                            <th>Image</th>
                            <th>Name</th>
                            <th>Author</th>
                            <th>Price</th>
                            <th>Borrow Days</th>
                            <th> </th>
                        </tr>
                        </thead>
                        <tbody>
                        {cart?.items?.length > 0 ? (
                            cart.items.map((item, index) => (
                                    <CartBookDetailsCard cartElement={item} onRemove={handleRemoveItem} key={index} />
                            ))
                        ) : (
                                <SVGCartIsEmpty />
                        )}
                        </tbody>
                    </table>
                    <div className="d-flex justify-content-center">
                        <button className="btn col-sm-4 green-btn" onClick={handlePurchase}>Purchase</button>

                    </div>
                </div>
            </div>

            <Footer />
        </>
    );
};

export default BuyCartPage;