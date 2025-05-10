import React from 'react';
import { useNavigate } from 'react-router-dom';
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

            if (!response.ok) {
                console.error(`Failed to remove cart item: ${response.status}`);
                const responseData = await response.json();
                navigate('/error', { state: { message: responseData.message } });
                return;
            }

            const responseData = await response.json();
            // SAVE the response data into a file
            const blob = new Blob([JSON.stringify(responseData, null, 2)], { type: 'text/plain' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'backend_response.txt'; // file name
            a.click();
            URL.revokeObjectURL(url);
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
          <td>{cartElement?.title}</td>
          <td>{cartElement?.author}</td>
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