import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';
import designBook from '../../../src/images/designBook.jpg';

function CartBookDetailsCard({cartElement, onRemove}) {
    const navigate = useNavigate();

    const removeHandler = async () => {
        try {
            const response = await fetch(`/api/carts/remove`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ title: cartElement?.title }),
            });

            const responseData = await response.json();
            if (responseData.status === 'success') {
                onRemove();
            } else {
                console.error(responseData.message || 'Failed to remove cart item.');
                navigate('/error', { state: { message: responseData.message || 'Failed to remove cart item.' } });
            }
        } catch (err) {
            console.error('Fetch error:', err);
            navigate('/error', { state: { message: 'An error occurred while trying to remove the cart item.' } });
        }
    };

    return (
        <tr>
          <td><img src={designBook} className="product-img" alt="Book" /></td>
          <td><Link 
              to={`/book-details?title=${encodeURIComponent(cartElement?.title)}`} className="link-text">
              {cartElement?.title}
          </Link></td>
          <td><Link to={`/author?name=${encodeURIComponent(cartElement?.author)}`} className="link-text">
              {cartElement?.author}
          </Link></td> 
          <td>
            {cartElement?.isBorrowed ? (
              <>
                <span style={{ textDecoration: 'line-through', marginRight: '6px' }}>${cartElement?.price}</span>
                <span> ${cartElement?.finalPrice}</span>
              </>
            ) : (
              <span>${cartElement?.finalPrice}</span>
            )}
          </td>
          <td>
            {cartElement?.isBorrowed
              ? `${cartElement?.borrowDays}`
              : "Not Borrowed"}
          </td>
          <th>
            <button className="btn gray-btn col-sm-12" onClick={removeHandler}>Remove</button>
          </th>
        </tr>
    );
};

export default CartBookDetailsCard;