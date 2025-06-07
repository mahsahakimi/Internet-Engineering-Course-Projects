import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import '../../stylesheets/General.css';
import '../../stylesheets/HistoryItem.css';


const HistoryItem = ({ date, items, totalCost }) => {
    const [isOpen, setIsOpen] = useState(false);
    
    const formattedDate = `${date.year}-${String(date.month).padStart(2, '0')}-${String(date.day).padStart(2, '0')} ${String(date.hour).padStart(2, '0')}:${String(date.minute).padStart(2, '0')}`;

    return (
        <div className="history-item">
            <div className="history-header" onClick={() => setIsOpen(!isOpen)}>
                <span>{formattedDate} | ${totalCost}</span>
                
                <i className={`bi ${isOpen ? 'bi-chevron-up' : 'bi-chevron-down'}`}></i>
            </div>

            {isOpen && (
                <div className="history-details">
                    <table className="table">
                        <thead className="table-light">
                            <tr>
                                <th>Image</th>
                                <th>Name</th>
                                <th>Author</th>
                                <th>Price</th>
                                <th>Borrow Days</th>
                            </tr>
                        </thead>
                        <tbody>
                            {items.map((item, idx) => (
                                <tr key={idx}>
                                    <td>
                                        <img src="/images/book.png" alt="cover" className="book-thumbnail" />
                                    </td>
                                    <td><Link 
                                        to={`/book-details?title=${encodeURIComponent(item?.title)}`} className="link-text">
                                        {item.title}
                                    </Link></td>
                                    <td><Link to={`/author?name=${encodeURIComponent(item?.author)}`} className="link-text">
                                        {item.author}
                                    </Link></td>  
                                    <td>
                                        {item.price !== item.finalPrice ? (
                                            <>
                                                <span className="text-decoration-line-through">${item.price}</span>
                                                <span className="ms-1 text-success">${item.finalPrice}</span>
                                            </>
                                        ) : (
                                            `$${item.price}`
                                        )}
                                    </td>
                                    <td>{item.isBorrowed ? item.borrowDays : 'Not Borrowed'}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default HistoryItem;