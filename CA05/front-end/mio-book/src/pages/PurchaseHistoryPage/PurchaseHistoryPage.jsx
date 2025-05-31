import React, { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../../UserContex';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import EmptySpace from '../../components/EmptySpace';
import HistoryItem from '../../components/HistoryItem';
import SVGNoPurchase from '../../components/SVGNoPurchase';

function PurchaseHistoryPage() {
    const { user, setUser } = useContext(UserContext);
    const [history, setHistory] = useState([]);
    const navigate = useNavigate();

    if (!user) {
        const localUser = JSON.parse(localStorage.getItem('activeUser'));
        setUser(localUser);
    }

    const fetchHistory = async () => {
        try {
            const response = await fetch('/api/users/purchase_history');
            const responseData = await response.json();

            if (responseData.status === "success") {
                setHistory(responseData.data.purchaseHistory);

            } else {
                navigate('/error', { state: { message: responseData.message || "Failed to fetch Purchase History." } });
            }
        } catch (error) {
            navigate('/error', { state: { message: error.message } });
        }
    };

    useEffect(() => {
        if (user?.userInfo?.role == "ADMIN") {
            navigate("/error", { state: { message: "You can not access this page. You are Admin!" } });
            return;
        }
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
                            <i className="bi bi-clock-history"></i> History
                        </h4>
                    </div>
                    <div className="history-list">
                        {history.length === 0 ? (
                            <div className="empty-state">
                                <SVGNoPurchase />
                                <p className='empty-message'>No purchase history available.</p>
                            </div>
                        ) : (
                            history.map((entry, idx) => (
                                <HistoryItem
                                    key={idx}
                                    date={entry.date}
                                    items={entry.items}
                                    totalCost={entry.totalCost}
                                />
                            ))
                        )}
                    </div>
                </div>
            </div>
            <Footer />
        </>
    );
}

export default PurchaseHistoryPage;